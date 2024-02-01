package org.sopt.makers.operation.dto.attendance.request;

import org.operation.attendance.AttendanceStatus;

public record SubAttendanceUpdateRequestDTO(
	Long subAttendanceId,
	AttendanceStatus status
) {
}
