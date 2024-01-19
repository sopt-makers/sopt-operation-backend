package org.sopt.makers.operation.service;

import static java.util.Objects.nonNull;
import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.sopt.makers.operation.entity.AttendanceStatus.*;
import static org.sopt.makers.operation.entity.alarm.Attribute.*;
import static org.sopt.makers.operation.entity.lecture.LectureStatus.*;

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
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.SubLectureException;
import org.sopt.makers.operation.external.api.AlarmSender;
import org.sopt.makers.operation.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.repository.SubAttendanceRepository;
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
	public LectureResponseDTO getLecture(Long lectureId) {
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

	private SubLecture getSubLecture(Lecture lecture, int round) {
		return lecture.getSubLectures().stream()
				.filter(l -> l.getRound() == round)
				.findFirst()
				.orElseThrow(() -> new SubLectureException(NO_SUB_LECTURE_EQUAL_ROUND.getName()));
	}

	@Override
	@Transactional
	public void endLecture(Long lectureId) {
		val lecture = findLecture(lectureId);
		checkEndLectureValidity(lecture);
		lecture.updateToEnd();
		sendAlarm(lecture);
	}

	/** SCHEDULER **/

	@Override
	@Transactional
	public void endLectures() {
		val lectures = lectureRepository.findLecturesToBeEnd();
		lectures.forEach(lecture -> endLecture(lecture.getId()));
	}

	private void checkEndLectureValidity(Lecture lecture) {
		val now = LocalDateTime.now();
		if (now.isBefore(lecture.getEndDate())) {
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
	public LectureGetResponseDTO getCurrentLecture(Long playGroundId) {
		val now = LocalDateTime.now();

		val attendances = attendanceRepository.findCurrentAttendanceByMember(playGroundId);

		if (attendances.isEmpty()) {
			return new LectureGetResponseDTO(LectureResponseType.NO_SESSION, 0L, "", "", "", "", "", Collections.emptyList());
		}

		if (attendances.size() > 2) {
			throw new LectureException(INVALID_COUNT_SESSION.getName());
		}

		// 현재 출석과 Lecture 가져오기
		val currentAttendance = getCurrentAttendance(attendances, now);
		val currentLecture = currentAttendance.getLecture();
		val lectureType = getLectureResponseType(currentLecture);

		if (lectureType.equals(LectureResponseType.NO_ATTENDANCE)) {
			val message = "출석 점수가 반영되지 않아요.";
			return LectureGetResponseDTO.of(lectureType, currentLecture, message, Collections.emptyList());
		}

		val subAttendances = attendanceRepository.findSubAttendanceByAttendanceId(currentAttendance.getId());

		val firstSubLectureAttendance = subAttendances.get(0);
		val secondSubLectureAttendance = subAttendances.get(1);

		val firstSubLectureAttendanceStatus = firstSubLectureAttendance.getStatus();
		val secondSubLectureAttendanceStatus = secondSubLectureAttendance.getStatus();

		val firstSessionStart = firstSubLectureAttendance.getSubLecture().getStartAt();
		val secondSessionStart = secondSubLectureAttendance.getSubLecture().getStartAt();

		val message = (currentLecture.getAttribute() == Attribute.SEMINAR) ? "" : "행사도 참여하고, 출석점수도 받고, 일석이조!";

		// Lecture 시작 전 혹은 1차 출석 시작 전
		if (now.isBefore(currentLecture.getStartDate()) || !nonNull(firstSessionStart)) {
			return LectureGetResponseDTO.of(lectureType, currentLecture, message, Collections.emptyList());
		}

		// 1차 출석 시작, 2차 출석 시작 전
		if (now.isAfter(firstSessionStart) && !nonNull(secondSessionStart)) {
			// 1차 출석 중 결석인 상태
			if (now.isBefore(firstSessionStart.plusMinutes(10)) && firstSubLectureAttendanceStatus.equals(ABSENT)) {
				return LectureGetResponseDTO.of(lectureType, currentLecture, message, Collections.emptyList());
			}

			return LectureGetResponseDTO.of(lectureType, currentLecture, message, Collections.singletonList(firstSubLectureAttendance));
		}

		// 2차 출석 시작 이후
		if (now.isAfter(secondSessionStart)) {
			// 2차 출석 중 결석인 상태
			if (now.isBefore(secondSessionStart.plusMinutes(10)) && secondSubLectureAttendanceStatus.equals(ABSENT)) {
				return LectureGetResponseDTO.of(lectureType, currentLecture, message, Collections.singletonList(firstSubLectureAttendance));
			}
		}
		return LectureGetResponseDTO.of(lectureType, currentLecture, message, subAttendances);
	}

	@Override
	public LectureCurrentRoundResponseDTO getCurrentLectureRound(Long lectureId) {
		val now = LocalDateTime.now();
		val today = now.toLocalDate();
		val startOfDay = today.atStartOfDay();
		val endOfDay = LocalDateTime.of(today, LocalTime.MAX);

		val lecture = lectureRepository.findById(lectureId)
				.orElseThrow(() -> new LectureException(INVALID_LECTURE.getName()));

		val lectureStartDate = lecture.getStartDate();
		val lectureStatus = lecture.getLectureStatus();

		val subLectures = lecture.getSubLectures();
		subLectures.sort(Comparator.comparing(SubLecture::getRound));

		val subLecture = lectureStatus.equals(FIRST) ?
				subLectures.get(0) : subLectures.get(1);

		if (lectureStartDate.isBefore(startOfDay) || lectureStartDate.isAfter(endOfDay)) {
			throw new LectureException(NO_SESSION.getName());
		}

		if (lectureStatus.equals(BEFORE)) {
			throw new LectureException(NOT_STARTED_ATTENDANCE.getName());
		}

		if (lectureStatus.equals(FIRST)) {
			// 1차 출석이 마감되었을 때
			if (now.isAfter(subLecture.getStartAt().plusMinutes(10))) {
				throw new LectureException(subLecture.getRound() + ENDED_ATTENDANCE.getName());
			}
		}

		if (lectureStatus.equals(SECOND)) {
			// 2차 출석이 마감되었을 때
			if (now.isAfter(subLecture.getStartAt().plusMinutes(10))) {
				throw new LectureException(subLecture.getRound() + ENDED_ATTENDANCE.getName());
			}
		}

		if (lectureStatus.equals(END)) {
			throw new LectureException(END_LECTURE.getName());
		}

		return LectureCurrentRoundResponseDTO.of(subLecture);
	}

	@Override
	@Transactional
	public void deleteLecture(Long lectureId) {
		val lecture = lectureRepository.find(lectureId)
			.orElseThrow(() -> new LectureException(INVALID_LECTURE.getName()));

		// 출석 종료된 세션: 출석 점수 갱신 전으로 복구
		if (lecture.getLectureStatus().equals(END)) {
			lecture.getAttendances().forEach(Attendance::revertMemberScore);
		}

		// 연관 관계의 객체 삭제 후 세션 삭제
		subAttendanceRepository.deleteAllBySubLectureIn(lecture.getSubLectures());
		subLectureRepository.deleteAllByLecture(lecture);
		attendanceRepository.deleteAllByLecture(lecture);
		lectureRepository.deleteById(lectureId);
		// lectureRepository.delete(lecture); //TODO: 에러 원인 파악 필요
	}

	@Override
	public LectureDetailResponseDTO getLectureDetail(Long lectureId) {
		val lecture = findLecture(lectureId);
		return LectureDetailResponseDTO.of(lecture);
	}

	private Lecture findLecture(Long id) {
		return lectureRepository.findById(id)
			.orElseThrow(() -> new LectureException(INVALID_LECTURE.getName()));
	}

	private Attendance getCurrentAttendance(List<Attendance> attendances, LocalDateTime now) {
		val lectureSize = attendances.size();
		val currentHour = now.getHour();

		val attendanceIndex = (lectureSize == 2 && currentHour >= 16) ? 1 : 0;
		return attendances.get(attendanceIndex);
	}

	private LectureResponseType getLectureResponseType(Lecture currentLecture) {
		return (currentLecture.getAttribute() != Attribute.ETC) ? LectureResponseType.HAS_ATTENDANCE : LectureResponseType.NO_ATTENDANCE;
	}
}
