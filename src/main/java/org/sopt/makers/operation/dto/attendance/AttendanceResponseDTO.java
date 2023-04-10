package org.sopt.makers.operation.dto.attendance;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.SubAttendance;

public record AttendanceResponseDTO(
	Long subAttendanceId,
	AttendanceStatus status
) {
	public static AttendanceResponseDTO of(SubAttendance subAttendance) {
		return new AttendanceResponseDTO(subAttendance.getId(), subAttendance.getStatus());
	}
}
