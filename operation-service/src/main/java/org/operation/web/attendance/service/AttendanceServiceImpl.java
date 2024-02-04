package org.operation.web.attendance.service;

import static org.operation.attendance.message.ErrorMessage.*;
import static org.operation.member.message.ErrorMessage.*;

import org.operation.attendance.domain.SubAttendance;
import org.operation.attendance.repository.attendance.AttendanceRepository;
import org.operation.attendance.repository.subAttendance.SubAttendanceRepository;
import org.operation.common.domain.Part;
import org.operation.common.exception.LectureException;
import org.operation.common.exception.MemberException;
import org.operation.member.domain.Member;
import org.operation.member.repository.MemberRepository;
import org.operation.web.attendance.dto.request.SubAttendanceUpdateRequest;
import org.operation.web.attendance.dto.response.AttendanceMemberResponse;
import org.operation.web.attendance.dto.response.AttendancesResponse;
import org.operation.web.attendance.dto.response.SubAttendanceUpdateResponse;
import org.operation.web.attendnace.service.AttendanceService;
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

	private final SubAttendanceRepository subAttendanceRepository;
	private final MemberRepository memberRepository;
	private final AttendanceRepository attendanceRepository;

	@Override
	@Transactional
	public SubAttendanceUpdateResponse updateSubAttendance(SubAttendanceUpdateRequest request) {
		val subAttendance = findSubAttendance(request.subAttendanceId());
		subAttendance.updateStatus(request.status());
		return SubAttendanceUpdateResponse.of(subAttendance);
	}

	private SubAttendance findSubAttendance(Long id) {
		return subAttendanceRepository.findById(id)
				.orElseThrow(() -> new LectureException(INVALID_ATTENDANCE.getContent()));
	}

	@Override
	public AttendanceMemberResponse findAttendancesByMember(long memberId) {
		val member = findMember(memberId);
		val attendances = attendanceRepository.findByMember(member);
		return AttendanceMemberResponse.of(member, attendances);
	}

	private Member findMember(Long id) {
		return memberRepository.findById(id)
				.orElseThrow(() -> new MemberException(INVALID_MEMBER.getContent()));
	}

	@Override
	@Transactional
	public float updateMemberScore(Long memberId) {
		val member = memberRepository.find(memberId)
			.orElseThrow(() -> new MemberException(INVALID_MEMBER.getContent()));
		member.updateTotalScore();
		return member.getScore();
	}

	@Override
	public AttendancesResponse findAttendancesByLecture(long lectureId, Part part, Pageable pageable) {
		val attendances = attendanceRepository.findByLecture(lectureId, part, pageable);
		val attendancesCount = attendanceRepository.countByLectureIdAndPart(lectureId, part);
		return AttendancesResponse.of(attendances, attendancesCount);
	}
}
