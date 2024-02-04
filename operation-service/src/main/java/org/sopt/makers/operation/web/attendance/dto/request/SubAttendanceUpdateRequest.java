package org.sopt.makers.operation.web.attendance.dto.request;

import org.operation.attendance.domain.AttendanceStatus;

public record SubAttendanceUpdateRequest(
	Long subAttendanceId,
	AttendanceStatus status
) {
}
