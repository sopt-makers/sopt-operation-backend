package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.attendance.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceResponseDTO;

public interface AttendanceService {
	AttendanceResponseDTO updateAttendanceStatus(AttendanceRequestDTO requestDTO);
	AttendanceMemberResponseDTO getMemberAttendance(Long memberId);
	float updateMemberScore(Long memberId);
}
