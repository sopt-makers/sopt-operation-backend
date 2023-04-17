package org.sopt.makers.operation.service;

import java.util.List;

import org.sopt.makers.operation.dto.attendance.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.attendance.MemberResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.springframework.data.domain.Pageable;
import org.sopt.makers.operation.dto.attendance.*;

public interface AttendanceService {
	AttendanceResponseDTO updateAttendanceStatus(AttendanceRequestDTO requestDTO);
	AttendanceMemberResponseDTO getMemberAttendance(Long memberId);
	float updateMemberScore(Long memberId);
	List<MemberResponseDTO> getMemberAttendances(Long lectureId, Part part, Pageable pageable);
	AttendResponseDTO attend(Long memberId, AttendRequestDTO requestDTO);
}
