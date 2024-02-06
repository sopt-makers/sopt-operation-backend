package org.sopt.makers.operation.web.lecture.service;

import static org.operation.alarm.domain.Attribute.*;
import static org.operation.lecture.message.ErrorMessage.*;

import java.util.List;
import java.util.stream.Stream;

import org.operation.attendance.domain.Attendance;
import org.operation.attendance.domain.SubAttendance;
import org.operation.attendance.repository.attendance.AttendanceRepository;
import org.operation.attendance.repository.subAttendance.SubAttendanceRepository;
import org.operation.client.alarm.AlarmSender;
import org.sopt.makers.operation.common.config.ValueConfig;
import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.common.exception.LectureException;
import org.sopt.makers.operation.common.exception.SubLectureException;
import org.operation.lecture.Lecture;
import org.operation.lecture.SubLecture;
import org.operation.lecture.repository.lecture.LectureRepository;
import org.operation.lecture.repository.subLecture.SubLectureRepository;
import org.operation.member.domain.Member;
import org.operation.member.repository.MemberRepository;
import org.sopt.makers.operation.web.lecture.dto.request.AttendanceRequest;
import org.sopt.makers.operation.web.lecture.dto.request.LectureRequest;
import org.sopt.makers.operation.web.lecture.dto.response.AttendanceResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureDetailResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LecturesResponse;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmSenderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;

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

	@Override
	@Transactional
	public long createLecture(LectureRequest request) {
		val savedLecture = saveLecture(request);
		createSubLectures(savedLecture);
		createAttendance(request.generation(), request.part(), savedLecture);
		createSubAttendances(savedLecture);
		return savedLecture.getId();
	}

	private Lecture saveLecture(LectureRequest request) {
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
	public LecturesResponse getLectures(int generation, Part part) {
		val lectures = lectureRepository.find(generation, part);
		return LecturesResponse.of(generation, lectures);
	}

	@Override
	public LectureResponse getLecture(long lectureId) {
		Lecture lecture = findLecture(lectureId);
		return LectureResponse.of(lecture);
	}

	@Override
	@Transactional
	public AttendanceResponse startAttendance(AttendanceRequest request) {
		val lecture = findLecture(request.lectureId());
		checkStartAttendanceValidity(lecture, request.round());
		val subLecture = getSubLecture(lecture, request.round());
		subLecture.startAttendance(request.code());
		return AttendanceResponse.of(lecture, subLecture);
	}

	private void checkStartAttendanceValidity(Lecture lecture, int round) {
		if (lecture.isEnd()) {
			throw new LectureException(END_LECTURE.getContent());
		} else if (round == 2 && lecture.isBefore()) {
			throw new LectureException(NOT_STARTED_PRE_ATTENDANCE.getContent());
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
	public LectureDetailResponse getLectureDetail(long lectureId) {
		val lecture = findLecture(lectureId);
		return LectureDetailResponse.of(lecture);
	}

	private Lecture findLecture(Long id) {
		return lectureRepository.findById(id)
				.orElseThrow(() -> new LectureException(INVALID_LECTURE.getContent()));
	}

	private void checkEndLectureValidity(Lecture lecture) {
		if (!lecture.isEnd()) {
			throw new LectureException(NOT_END_TIME_YET.getContent());
		}
	}

	private void sendAlarm(Lecture lecture) {
		val alarmTitle = getAlarmTitle(lecture);
		val alarmContent = valueConfig.getALARM_MESSAGE_CONTENT();
		val memberPlaygroundIds = getMemberPlaygroundIds(lecture);
		alarmSender.send(new AlarmSenderRequest(alarmTitle, alarmContent, memberPlaygroundIds, NEWS, null));
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

	private SubLecture getSubLecture(Lecture lecture, int round) {
		return lecture.getSubLectures().stream()
				.filter(l -> l.getRound() == round)
				.findFirst()
				.orElseThrow(() -> new SubLectureException(NO_SUB_LECTURE_EQUAL_ROUND.getContent()));
	}

}
