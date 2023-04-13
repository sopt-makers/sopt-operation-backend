package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.sopt.makers.operation.entity.AttendanceStatus.*;
import static org.sopt.makers.operation.entity.lecture.Attribute.*;

import javax.persistence.EntityNotFoundException;

import org.sopt.makers.operation.dto.attendance.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceResponseDTO;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.repository.SubAttendanceRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.sopt.makers.operation.util.Generation32;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

	private final SubAttendanceRepository subAttendanceRepository;
	private final MemberRepository memberRepository;
	private final Generation32 sopt32;

	@Override
	@Transactional
	public AttendanceResponseDTO updateAttendanceStatus(AttendanceRequestDTO requestDTO) {
		SubAttendance subAttendance = findSubAttendance(requestDTO.subAttendanceId());
		subAttendance.updateStatus(requestDTO.status());
		Attendance attendance = subAttendance.getAttendance();
		attendance.updateStatus(sopt32.getAttendanceStatus(requestDTO.attribute(), attendance.getSubAttendances()));
		return AttendanceResponseDTO.of(subAttendance);
	}

	@Override
	public AttendanceMemberResponseDTO getMemberAttendance(Long memberId) {
		Member member = findMember(memberId);
		return AttendanceMemberResponseDTO.of(member);
	}

	@Override
	@Transactional
	public float updateMemberScore(Long memberId) {
		Member member = findMember(memberId);
		float score = (float)(2 + member.getAttendances().stream()
			.mapToDouble(attendance -> getUpdateScore(attendance, attendance.getLecture().getAttribute()))
			.sum());
		member.setScore(score);
		return member.getScore();
	}

	private float getUpdateScore(Attendance attendance, Attribute attribute) {
		if (attribute.equals(SEMINAR)) {
			if (attendance.getStatus().equals(TARDY)) {
				return -0.5f;
			} else if (attendance.getStatus().equals(ABSENT)) {
				return -1;
			}
		} else if (attribute.equals(EVENT)) {
			if (attendance.getStatus().equals(ATTENDANCE)) {
				return 0.5f;
			}
		}
		return 0;
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
