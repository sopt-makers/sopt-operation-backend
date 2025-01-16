package org.sopt.makers.operation.web.attendnace.service;

import static org.sopt.makers.operation.code.failure.AttendanceFailureCode.*;
import static org.sopt.makers.operation.code.failure.LectureFailureCode.*;
import static org.sopt.makers.operation.code.failure.MemberFailureCode.*;

import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.attendance.domain.SubAttendance;
import org.sopt.makers.operation.attendance.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.attendance.repository.subAttendance.SubAttendanceRepository;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.repository.lecture.LectureRepository;
import org.sopt.makers.operation.member.domain.Member;
import org.sopt.makers.operation.member.repository.MemberRepository;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.web.attendnace.dto.request.SubAttendanceUpdateRequest;
import org.sopt.makers.operation.web.attendnace.dto.response.AttendanceListByMemberGetResponse;
import org.sopt.makers.operation.web.attendnace.dto.response.AttendanceListByLectureGetResponse;
import org.sopt.makers.operation.web.attendnace.dto.response.MemberScoreUpdateResponse;
import org.sopt.makers.operation.web.attendnace.dto.response.SubAttendanceUpdateResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebAttendanceServiceImpl implements WebAttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final SubAttendanceRepository subAttendanceRepository;
	private final MemberRepository memberRepository;
	private final LectureRepository lectureRepository;

	@Override
	@Transactional
	public SubAttendanceUpdateResponse updateSubAttendance(SubAttendanceUpdateRequest request) {
		val subAttendance = findSubAttendance(request.subAttendanceId());
		subAttendance.updateStatus(request.status());
		return SubAttendanceUpdateResponse.of(subAttendance);
	}

	@Override
	public AttendanceListByMemberGetResponse getAttendancesByMember(long memberId) {
		val member = findMember(memberId);
		val attendances = attendanceRepository.findFetchJoin(member);
		return AttendanceListByMemberGetResponse.of(member, attendances);
	}

	@Override
	@Transactional
	public MemberScoreUpdateResponse updateMemberAllScore(long memberId) {
		val member = findMember(memberId);
		member.updateTotalScore();
		return MemberScoreUpdateResponse.of(member);
	}

	@Override
	public AttendanceListByLectureGetResponse getAttendancesByLecture(long lectureId, Part part, Pageable pageable) {
		val lecture = findLecture(lectureId);
		val attendances = attendanceRepository.findFetchJoin(lecture, part, pageable);
		val totalCount = attendanceRepository.count(lecture, part);
		return AttendanceListByLectureGetResponse.of(attendances, totalCount);
	}

	private SubAttendance findSubAttendance(long id) {
		return subAttendanceRepository.findById(id)
				.orElseThrow(() -> new LectureException(INVALID_ATTENDANCE));
	}

	private Member findMember(long id) {
		return memberRepository.findById(id)
				.orElseThrow(() -> new MemberException(INVALID_MEMBER));
	}

	private Lecture findLecture(long id) {
		return lectureRepository.findById(id)
				.orElseThrow(() -> new LectureException(INVALID_LECTURE));
	}
}
