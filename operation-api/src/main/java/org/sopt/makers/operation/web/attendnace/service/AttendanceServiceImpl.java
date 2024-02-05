package org.sopt.makers.operation.web.attendnace.service;

import static org.sopt.makers.operation.code.failure.AttendanceFailureCode.*;
import static org.sopt.makers.operation.code.failure.MemberFailureCode.*;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.domain.attendance.domain.SubAttendance;
import org.sopt.makers.operation.domain.attendance.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.domain.attendance.repository.subAttendance.SubAttendanceRepository;
import org.sopt.makers.operation.domain.member.domain.Member;
import org.sopt.makers.operation.domain.member.repository.MemberRepository;
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
	public float updateMemberScore(Long memberId) {
		val member = findMember(memberId);
		member.updateTotalScore();
		return member.getScore();
	}

	@Override
	public AttendanceListResponse findAttendancesByLecture(long lectureId, Part part, Pageable pageable) {
		val attendances = attendanceRepository.findByLecture(lectureId, part, pageable);
		val attendancesCount = attendanceRepository.countByLectureIdAndPart(lectureId, part);
		return AttendanceListResponse.of(attendances, attendancesCount);
	}
}
