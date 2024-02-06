package org.sopt.makers.operation.web.lecture.service;

import static org.sopt.makers.operation.code.failure.LectureFailureCode.*;

import java.util.List;
import java.util.stream.Stream;

import org.sopt.makers.operation.client.alarm.AlarmSender;
import org.sopt.makers.operation.client.alarm.dto.AlarmSenderRequest;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.domain.attendance.domain.Attendance;
import org.sopt.makers.operation.domain.attendance.domain.SubAttendance;
import org.sopt.makers.operation.domain.attendance.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.domain.attendance.repository.subAttendance.SubAttendanceRepository;
import org.sopt.makers.operation.domain.lecture.Lecture;
import org.sopt.makers.operation.domain.lecture.SubLecture;
import org.sopt.makers.operation.domain.lecture.repository.lecture.LectureRepository;
import org.sopt.makers.operation.domain.lecture.repository.subLecture.SubLectureRepository;
import org.sopt.makers.operation.domain.member.domain.Member;
import org.sopt.makers.operation.domain.member.repository.MemberRepository;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.SubLectureException;
import org.sopt.makers.operation.web.lecture.dto.request.AttendanceRequest;
import org.sopt.makers.operation.web.lecture.dto.request.LectureRequest;
import org.sopt.makers.operation.web.lecture.dto.response.AttendanceResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureDetailResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureListResponse;
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
		saveAttendances(request.generation(), request.part(), savedLecture);
		return savedLecture.getId();
	}

	private Lecture saveLecture(LectureRequest request) {
		val savedLecture = lectureRepository.save(request.toEntity());
		saveSubLectures(savedLecture);
		return savedLecture;
	}

	private void saveSubLectures(Lecture lecture) {
		val maxRound = valueConfig.getSUB_LECTURE_MAX_ROUND();
		Stream.iterate(1, i -> i + 1).limit(maxRound)
				.forEach(round -> saveSubLecture(lecture, round));
	}

	private void saveSubLecture(Lecture lecture, int round) {
		subLectureRepository.save(new SubLecture(lecture, round));
	}

	private void saveAttendances(int generation, Part part, Lecture lecture) {
		memberRepository.find(generation, part)
				.forEach(member -> saveAttendance(member, lecture));
	}

	private void saveAttendance(Member member, Lecture lecture) {
		val savedAttendance = attendanceRepository.save(new Attendance(member, lecture));
		saveSubAttendances(savedAttendance);
	}

	private void saveSubAttendances(Attendance attendance) {
		attendance.getLecture().getSubLectures()
				.forEach(subLecture -> saveSubAttendance(attendance, subLecture));
	}

	private void saveSubAttendance(Attendance attendance, SubLecture subLecture) {
		subAttendanceRepository.save(new SubAttendance(attendance, subLecture));
	}

	@Override
	public LectureListResponse getLectures(int generation, Part part) {
		val lectureList = findLectures(generation, part);
		return LectureListResponse.of(generation, lectureList);
	}

	private List<Lecture> findLectures(int generation, Part part) {
		return lectureRepository.find(generation, part);
	}

	@Override
	public LectureResponse getLecture(long lectureId) {
		val lecture = findLecture(lectureId);
		return LectureResponse.of(lecture);
	}

	private Lecture findLecture(long id) {
		return lectureRepository.findById(id)
				.orElseThrow(() -> new LectureException(INVALID_LECTURE));
	}

	@Override
	@Transactional
	public AttendanceResponse startAttendance(AttendanceRequest request) {
		val lecture = getLectureToStartAttendance(request.lectureId(), request.round());
		val subLecture = getSubLectureToStartAttendance(lecture, request.round());
		subLecture.startAttendance(request.code());
		return AttendanceResponse.of(lecture, subLecture);
	}

	private Lecture getLectureToStartAttendance(long lectureId, int round) {
		val lecture = findLecture(lectureId);
		checkStartAttendanceValidity(lecture, round);
		return lecture;
	}

	private void checkStartAttendanceValidity(Lecture lecture, int round) {
		if (lecture.isEnd()) {
			throw new LectureException(END_LECTURE);
		} else if (round == 2 && lecture.isBefore()) {
			throw new LectureException(NOT_STARTED_PRE_ATTENDANCE);
		}
	}

	private SubLecture getSubLectureToStartAttendance(Lecture lecture, int round) {
		return lecture.getSubLectures().stream()
				.filter(l -> l.getRound() == round)
				.findFirst()
				.orElseThrow(() -> new SubLectureException(NO_SUB_LECTURE_EQUAL_ROUND));
	}

	@Override
	@Transactional
	public void endLecture(long lectureId) {
		val lecture = getLectureToEnd(lectureId);
		lecture.updateToEnd();
		sendAlarm(lecture);
	}

	private Lecture getLectureToEnd(long lectureId) {
		val lecture = findLecture(lectureId);
		checkEndLectureValidity(lecture);
		return lecture;
	}

	private void checkEndLectureValidity(Lecture lecture) {
		if (!lecture.isEnd()) {
			throw new LectureException(NOT_END_TIME_YET);
		}
	}

	private void sendAlarm(Lecture lecture) {
		val alarmRequest = AlarmSenderRequest.of(lecture, valueConfig);
		alarmSender.send(alarmRequest);
	}

	@Override
	@Transactional
	public void deleteLecture(long lectureId) {
		val lecture = getLectureToDelete(lectureId);
		deleteRelationship(lecture);
		lectureRepository.deleteById(lectureId);
	}

	private Lecture getLectureToDelete(long lectureId) {
		val lecture = findLecture(lectureId);
		if (lecture.isEnd()) {
			restoreAttendances(lecture.getAttendances());
		}
		return lecture;
	}

	private void restoreAttendances(List<Attendance> attendances) {
		attendances.forEach(Attendance::restoreMemberScore);
	}

	private void deleteRelationship(Lecture lecture) {
		subAttendanceRepository.deleteAllBySubLectureIn(lecture.getSubLectures());
		subLectureRepository.deleteAllByLecture(lecture);
		attendanceRepository.deleteAllByLecture(lecture);
	}

	@Override
	public LectureDetailResponse getLectureDetail(long lectureId) {
		val lecture = findLecture(lectureId);
		return LectureDetailResponse.of(lecture);
	}

}
