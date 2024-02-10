package org.sopt.makers.operation.app.attendance.service;

import static org.sopt.makers.operation.attendance.domain.AttendanceStatus.*;
import static org.sopt.makers.operation.code.failure.member.memberFailureCode.*;
import static org.sopt.makers.operation.code.failure.subAttendance.subAttendanceFailureCode.*;
import static org.sopt.makers.operation.code.failure.lecture.LectureFailureCode.*;
import static org.sopt.makers.operation.code.failure.subLecture.subLectureFailureCode.*;

import org.sopt.makers.operation.app.attendance.dto.request.AttendanceRequest;
import org.sopt.makers.operation.app.attendance.dto.response.AttendanceResponse;
import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.attendance.domain.SubAttendance;
import org.sopt.makers.operation.attendance.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.SubLectureException;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.domain.SubLecture;
import org.sopt.makers.operation.lecture.repository.subLecture.SubLectureRepository;
import org.sopt.makers.operation.member.domain.Member;
import org.sopt.makers.operation.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final MemberRepository memberRepository;
	private final SubLectureRepository subLectureRepository;
	private final ValueConfig valueConfig;

	@Override
	@Transactional
	public AttendanceResponse attend(long playgroundId, AttendanceRequest request) {
		val subAttendance = getSubAttendance(request.subLectureId(), request.code(), playgroundId);
		subAttendance.updateStatus(ATTENDANCE);
		return AttendanceResponse.of(subAttendance);
	}

	private SubAttendance getSubAttendance(long subLectureId, String code, long playgroundId) {
		val subLecture = getSubLecture(subLectureId, code);
		val attendance = getAttendance(subLecture, playgroundId);
		return getSubAttendance(attendance, subLecture.getRound());
	}

	private SubLecture getSubLecture(long subLectureId, String code) {
		val subLecture = findSubLecture(subLectureId);
		checkSubLectureValidity(subLecture);
		checkMatchedCode(subLecture, code);
		return subLecture;
	}

	private SubLecture findSubLecture(long subLectureId) {
		return subLectureRepository
				.findById(subLectureId)
				.orElseThrow(() -> new SubLectureException(INVALID_ATTENDANCE));
	}

	private void checkSubLectureValidity(SubLecture subLecture) {
		checkSubLectureStarted(subLecture);
		checkSubLectureEnded(subLecture);
	}

	private void checkSubLectureStarted(SubLecture subLecture) {
		if (subLecture.isNotStarted()) {
			throw new LectureException(NOT_STARTED_NTH_ATTENDANCE);
		}
	}

	private void checkSubLectureEnded(SubLecture subLecture) {
		val attendanceMinute = valueConfig.getATTENDANCE_MINUTE();
		if (subLecture.isEnded(attendanceMinute)) {
			throw new LectureException(ENDED_ATTENDANCE, subLecture.getRound());
		}
	}

	private void checkMatchedCode(SubLecture subLecture, String code) {
		if (subLecture.isMatchCode(code)) {
			throw new SubLectureException(INVALID_CODE);
		}
	}

	private Attendance getAttendance(SubLecture subLecture, long playgroundId) {
		val lecture = subLecture.getLecture();
		val generation = valueConfig.getGENERATION();
		val member = findMember(playgroundId, generation);
		return findAttendance(lecture, member);
	}

	private Member findMember(long playgroundId, int generation) {
		return memberRepository
				.getMemberByPlaygroundIdAndGeneration(playgroundId, generation)
				.orElseThrow(() -> new MemberException(INVALID_MEMBER));
	}

	private Attendance findAttendance(Lecture lecture, Member member) {
		return attendanceRepository.findByLectureAndMember(lecture, member)
				.orElseThrow(() -> new LectureException(INVALID_ATTENDANCE));
	}

	private SubAttendance getSubAttendance(Attendance attendance, int round) {
		return attendance.getSubAttendances().stream()
				.filter(subAttendance -> subAttendance.isMatchRound(round))
				.findFirst()
				.orElseThrow(() -> new SubLectureException(INVALID_SUB_ATTENDANCE));
	}
}
