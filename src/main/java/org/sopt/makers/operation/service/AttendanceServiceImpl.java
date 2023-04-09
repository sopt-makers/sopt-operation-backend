package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;

import javax.persistence.EntityNotFoundException;

import org.sopt.makers.operation.dto.attendance.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceResponseDTO;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.repository.SubAttendanceRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

	private final SubAttendanceRepository subAttendanceRepository;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public AttendanceResponseDTO updateAttendanceStatus(AttendanceRequestDTO requestDTO) {
		SubAttendance subAttendance = findSubAttendance(requestDTO.subAttendanceId());
		subAttendance.updateStatus(requestDTO.status());
		return AttendanceResponseDTO.of(subAttendance);
	}

	@Override
	public AttendanceMemberResponseDTO getMemberAttendance(Long memberId) {
		Member member = findMember(memberId);
		return AttendanceMemberResponseDTO.of(member);
	}

	private Member findMember(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_MEMBER.getName()));
	}

	private SubAttendance findSubAttendance(Long id) {
		return subAttendanceRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_ATTENDANCE.getName()));
	}
}
