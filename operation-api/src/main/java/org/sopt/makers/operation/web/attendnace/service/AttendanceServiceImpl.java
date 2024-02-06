package org.sopt.makers.operation.web.attendnace.service;

import static org.sopt.makers.operation.code.failure.AttendanceFailureCode.*;
import static org.sopt.makers.operation.code.failure.LectureFailureCode.*;
import static org.sopt.makers.operation.code.failure.MemberFailureCode.*;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.attendance.domain.SubAttendance;
import org.sopt.makers.operation.attendance.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.attendance.repository.subAttendance.SubAttendanceRepository;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.repository.lecture.LectureRepository;
import org.sopt.makers.operation.member.domain.Member;
import org.sopt.makers.operation.member.repository.MemberRepository;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.web.attendnace.dto.request.UpdatedSubAttendanceRequest;
import org.sopt.makers.operation.web.attendnace.dto.response.AttendanceMemberResponse;
import org.sopt.makers.operation.web.attendnace.dto.response.AttendanceListResponse;
import org.sopt.makers.operation.web.attendnace.dto.response.UpdatedSubAttendanceResponse;
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
public class AttendanceServiceImpl implements AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final SubAttendanceRepository subAttendanceRepository;
	private final MemberRepository memberRepository;
	private final LectureRepository lectureRepository;

	@Override
	@Transactional
	public UpdatedSubAttendanceResponse updateSubAttendance(UpdatedSubAttendanceRequest request) {
		val subAttendance = findSubAttendance(request.subAttendanceId());
		subAttendance.updateStatus(request.status());
		return UpdatedSubAttendanceResponse.of(subAttendance);
	}

	private SubAttendance findSubAttendance(long id) {
		return subAttendanceRepository.findById(id)
				.orElseThrow(() -> new LectureException(INVALID_ATTENDANCE));
	}

	@Override
	public AttendanceMemberResponse findAttendancesByMember(long memberId) {
		val member = findMember(memberId);
		val attendanceList = attendanceRepository.findFetchJoin(member);
		return AttendanceMemberResponse.of(member, attendanceList);
	}

	private Member findMember(long id) {
		return memberRepository.findById(id)
				.orElseThrow(() -> new MemberException(INVALID_MEMBER));
	}

	@Override
	@Transactional
	public float updateMemberAllScore(long memberId) {
		val member = findMember(memberId);
		member.updateTotalScore();
		return member.getScore();
	}

	@Override
	public AttendanceListResponse findAttendancesByLecture(long lectureId, Part part, Pageable pageable) {
		val lecture = findLecture(lectureId);
		val attendanceList = attendanceRepository.findFetchJoin(lecture, part, pageable);
		val totalCount = attendanceRepository.count(lecture, part);
		return AttendanceListResponse.of(attendanceList, totalCount);
	}

	private Lecture findLecture(long id) {
		return lectureRepository.findById(id)
				.orElseThrow(() -> new LectureException(INVALID_LECTURE));
	}
}
