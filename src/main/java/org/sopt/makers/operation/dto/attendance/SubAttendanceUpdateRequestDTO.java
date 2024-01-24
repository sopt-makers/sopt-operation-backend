package org.sopt.makers.operation.dto.attendance;

import org.sopt.makers.operation.entity.attendance.AttendanceStatus;

public record SubAttendanceUpdateRequestDTO(
	Long subAttendanceId,
	AttendanceStatus status
) {
}
