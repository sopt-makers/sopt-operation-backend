package org.sopt.makers.operation.dto.attendance;

import org.sopt.makers.operation.entity.AttendanceStatus;

public record AttendanceResponseDTO(
	Long memberId,
	AttendanceStatus status,
	float score,
	float totalScore
) {
}
