package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.attendance.*;

public interface AttendanceService {
	AttendanceResponseDTO updateAttendanceStatus(AttendanceRequestDTO requestDTO);
	AttendanceMemberResponseDTO getMemberAttendance(Long memberId);
	float updateMemberScore(Long memberId);
	AttendResponseDTO attend(Long memberId, AttendRequestDTO requestDTO);
}
