package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.sopt.makers.operation.common.ExceptionMessage.NO_SESSION;
import static org.sopt.makers.operation.dto.lecture.LectureResponseType.*;
import static org.sopt.makers.operation.entity.attendance.AttendanceStatus.*;
import static org.sopt.makers.operation.entity.alarm.Attribute.*;
import static org.sopt.makers.operation.entity.lecture.LectureStatus.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import lombok.val;

import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.dto.alarm.AlarmSenderDTO;
import org.sopt.makers.operation.dto.lecture.*;

import org.sopt.makers.operation.dto.lecture.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.entity.*;
import org.sopt.makers.operation.entity.attendance.Attendance;
import org.sopt.makers.operation.entity.attendance.SubAttendance;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.entity.lecture.SubLecture;
import org.sopt.makers.operation.entity.member.Member;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.SubLectureException;
import org.sopt.makers.operation.external.alarm.AlarmSender;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.attendance.SubAttendanceRepository;
import org.sopt.makers.operation.repository.lecture.LectureRepository;
import org.sopt.makers.operation.repository.lecture.SubLectureRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureServiceImpl implements LectureService {

	private final LectureRepository lectureRepository;
	private final SubLectureRepository subLectureRepository;
	private final AttendanceRepository attendanceRepository;
	private final SubAttendanceRepository subAttendanceRepository;
	private final MemberRepository memberRepository;
	private final AlarmSender alarmSender;
	private final ValueConfig valueConfig;

	/** WEB **/

	@Override
	@Transactional
	public long createLecture(LectureRequestDTO request) {
		val savedLecture = saveLecture(request);
		createSubLectures(savedLecture);
		createAttendance(request.generation(), request.part(), savedLecture);
		createSubAttendances(savedLecture);
		return savedLecture.getId();
	}

	private Lecture saveLecture(LectureRequestDTO request) {
		val lecture = request.toEntity();
		return lectureRepository.save(lecture);
	}

	private void createSubLectures(Lecture lecture) {
		Stream.iterate(1, i -> i + 1).limit(valueConfig.getSUB_LECTURE_MAX_ROUND())
				.forEach(round -> saveSubLecture(lecture, round));
	}

	private void saveSubLecture(Lecture lecture, int round) {
		subLectureRepository.save(new SubLecture(lecture, round));
	}

	private void createAttendance(int generation, Part part, Lecture lecture) {
		memberRepository.find(generation, part).forEach(member -> saveAttendance(member, lecture));
	}

	private void saveAttendance(Member member, Lecture lecture) {
		attendanceRepository.save(new Attendance(member, lecture));
	}

	private void createSubAttendances(Lecture lecture) {
		lecture.getAttendances().forEach(this::saveSubAttendances);
	}

	private void saveSubAttendances(Attendance attendance) {
		attendance.getLecture().getSubLectures().forEach(subLecture -> saveSubAttendance(attendance, subLecture));
	}

	private void saveSubAttendance(Attendance attendance, SubLecture subLecture) {
		subAttendanceRepository.save(new SubAttendance(attendance, subLecture));
	}

	@Override
	public LecturesResponseDTO getLectures(int generation, Part part) {
		val lectures = lectureRepository.find(generation, part);
		return LecturesResponseDTO.of(generation, lectures);
	}

	@Override
	public LectureResponseDTO getLecture(long lectureId) {
		Lecture lecture = findLecture(lectureId);
		return LectureResponseDTO.of(lecture);
	}

	@Override
	@Transactional
	public AttendanceResponseDTO startAttendance(AttendanceRequestDTO requestDTO) {
		val lecture = findLecture(requestDTO.lectureId());
		checkStartAttendanceValidity(lecture, requestDTO.round());
		val subLecture = getSubLecture(lecture, requestDTO.round());
		subLecture.startAttendance(requestDTO.code());
		return AttendanceResponseDTO.of(lecture, subLecture);
	}

	private void checkStartAttendanceValidity(Lecture lecture, int round) {
		if (lecture.isEnd()) {
			throw new LectureException(END_LECTURE.getName());
		} else if (round == 2 && lecture.isBefore()) {
			throw new LectureException(NOT_STARTED_PRE_ATTENDANCE.getName());
		}
	}

	@Override
	@Transactional
	public void endLecture(Long lectureId) {
		val lecture = findLecture(lectureId);
		checkEndLectureValidity(lecture);
		lecture.updateToEnd();
		sendAlarm(lecture);
	}

	@Override
	@Transactional
	public void deleteLecture(Long lectureId) {
		val lecture = findLecture(lectureId);
		if (lecture.isEnd()) {
			restoreAttendances(lecture.getAttendances());
		}
		subAttendanceRepository.deleteAllBySubLectureIn(lecture.getSubLectures());
		subLectureRepository.deleteAllByLecture(lecture);
		attendanceRepository.deleteAllByLecture(lecture);
		lectureRepository.deleteById(lectureId);
	}

	private void restoreAttendances(List<Attendance> attendances) {
		attendances.forEach(Attendance::revertMemberScore);
	}

	@Override
	public LectureDetailResponseDTO getLectureDetail(long lectureId) {
		val lecture = findLecture(lectureId);
		return LectureDetailResponseDTO.of(lecture);
	}

	private Lecture findLecture(Long id) {
		return lectureRepository.findById(id)
				.orElseThrow(() -> new LectureException(INVALID_LECTURE.getName()));
	}

	/** SCHEDULER **/

	@Override
	@Transactional
	public void endLectures() {
		val lectures = lectureRepository.findLecturesToBeEnd();
		lectures.forEach(lecture -> endLecture(lecture.getId()));
	}

	private void checkEndLectureValidity(Lecture lecture) {
		if (!lecture.isEnd()) {
			throw new LectureException(NOT_END_TIME_YET.getName());
		}
	}

	private void sendAlarm(Lecture lecture) {
		val alarmTitle = getAlarmTitle(lecture);
		val alarmContent = valueConfig.getALARM_MESSAGE_CONTENT();
		val memberPlaygroundIds = getMemberPlaygroundIds(lecture);
		alarmSender.send(new AlarmSenderDTO(alarmTitle, alarmContent, memberPlaygroundIds, NEWS, null));
	}

	private List<String> getMemberPlaygroundIds(Lecture lecture) {
		return lecture.getAttendances().stream()
				.map(attendance -> String.valueOf(attendance.getMember().getPlaygroundId()))
				.filter(id -> !id.equals("null"))
				.toList();
	}

	private String getAlarmTitle(Lecture lecture) {
		return lecture.getName() + " " + valueConfig.getALARM_MESSAGE_TITLE();
	}

	/** APP **/

	@Override
	public TodayLectureResponseDTO getTodayLecture(long memberPlaygroundId) {
		val attendances = attendanceRepository.findToday(memberPlaygroundId);
		checkAttendancesSize(attendances);

		if (attendances.isEmpty()) {
			return getEmptyResponse();
		}

		val attendance = getNowAttendance(attendances);
		val lecture = attendance.getLecture();
		val responseType = getResponseType(lecture);
		val message = getMessage(lecture.getAttribute());

		if (responseType.equals(NO_ATTENDANCE) || lecture.isBefore()) {
			return TodayLectureResponseDTO.of(responseType, lecture, message, Collections.emptyList());
		}

		val subAttendances = attendance.getSubAttendances();
		val subAttendance = lecture.isFirst() ? subAttendances.get(0) : subAttendances.get(1);
		return getTodayLectureResponse(subAttendance, responseType, lecture);
	}

	private TodayLectureResponseDTO getEmptyResponse() {
		return TodayLectureResponseDTO.builder()
				.type(LectureResponseType.NO_SESSION)
				.id(0L)
				.location("")
				.name("")
				.startDate("")
				.endDate("")
				.message("")
				.attendances(Collections.emptyList())
				.build();
	}

	private void checkAttendancesSize(List<Attendance> attendances) {
		if (attendances.size() > valueConfig.getSUB_LECTURE_MAX_ROUND()) {
			throw new LectureException(INVALID_COUNT_SESSION.getName());
		}
	}

	private Attendance getNowAttendance(List<Attendance> attendances) {
		val index = getAttendanceIndex();
		return attendances.get(index);
	}

	private int getAttendanceIndex() {
		return (LocalDateTime.now().getHour() >= 16) ? 1 : 0;
	}

	private LectureResponseType getResponseType(Lecture lecture) {
		val attribute = lecture.getAttribute();
		return attribute.equals(Attribute.ETC) ? NO_ATTENDANCE : HAS_ATTENDANCE;
	}

	private String getMessage(Attribute attribute) {
		return switch (attribute) {
			case SEMINAR -> valueConfig.getSEMINAR_MESSAGE();
			case EVENT -> valueConfig.getEVENT_MESSAGE();
			case ETC -> valueConfig.getETC_MESSAGE();
		};
	}

	private TodayLectureResponseDTO getTodayLectureResponse(SubAttendance subAttendance, LectureResponseType responseType, Lecture lecture) {
		val subLecture = subAttendance.getSubLecture();
		val isOnAttendanceCheck = LocalDateTime.now().isBefore(subLecture.getStartAt().plusMinutes(10));
		val message = getMessage(lecture.getAttribute());
		if (isOnAttendanceCheck && subAttendance.getStatus().equals(ABSENT)) {
			return TodayLectureResponseDTO.of(responseType, lecture, message, Collections.emptyList());
		}
		return TodayLectureResponseDTO.of(responseType, lecture, message, Collections.singletonList(subAttendance));
	}

	@Override
	public LectureCurrentRoundResponseDTO getCurrentLectureRound(long lectureId) {
		val lecture = findLecture(lectureId);
		val subLecture = getSubLecture(lecture);
		checkLectureExist(lecture);
		checkLectureBefore(lecture);
		checkEndAttendance(subLecture);
		checkLectureEnd(lecture);
		return LectureCurrentRoundResponseDTO.of(subLecture);
	}

	private SubLecture getSubLecture(Lecture lecture) {
		val status = lecture.getLectureStatus();
		val round = status.equals(FIRST) ? 1 : 2;
		return getSubLecture(lecture, round);
	}

	private SubLecture getSubLecture(Lecture lecture, int round) {
		return lecture.getSubLectures().stream()
				.filter(l -> l.getRound() == round)
				.findFirst()
				.orElseThrow(() -> new SubLectureException(NO_SUB_LECTURE_EQUAL_ROUND.getName()));
	}

	private void checkLectureExist(Lecture lecture) {
		val today = LocalDate.now();
		val startOfDay = today.atStartOfDay();
		val endOfDay = LocalDateTime.of(today, LocalTime.MAX);
		val startAt = lecture.getStartDate();
		if (startAt.isBefore(startOfDay) || startAt.isAfter(endOfDay)) {
			throw new LectureException(NO_SESSION.getName());
		}
	}

	private void checkLectureBefore(Lecture lecture) {
		if (lecture.isBefore()) {
			throw new LectureException(NOT_STARTED_ATTENDANCE.getName());
		}
	}

	private void checkEndAttendance(SubLecture subLecture) {
		if (isEndAttendance(subLecture)) {
			throw new LectureException(subLecture.getRound() + ENDED_ATTENDANCE.getName());
		}
	}

	private boolean isEndAttendance(SubLecture subLecture) {
		val status = subLecture.getLecture().getLectureStatus();
		if (LocalDateTime.now().isAfter(subLecture.getStartAt().plusMinutes(10))) {
			return status.equals(FIRST) || status.equals(SECOND);
		}
		return false;
	}

	private void checkLectureEnd(Lecture lecture) {
		if (lecture.isEnd()) {
			throw new LectureException(END_LECTURE.getName());
		}
	}
}
