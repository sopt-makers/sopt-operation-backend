package org.sopt.makers.operation.dto.attendance.request;

import org.sopt.makers.operation.entity.attendance.AttendanceStatus;

public record SubAttendanceUpdateRequestDTO(
	Long subAttendanceId,
	AttendanceStatus status
) {
}
