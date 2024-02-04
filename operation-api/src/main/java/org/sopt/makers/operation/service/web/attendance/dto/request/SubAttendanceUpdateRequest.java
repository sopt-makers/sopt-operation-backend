package org.sopt.makers.operation.service.web.attendance.dto.request;

import org.operation.attendance.domain.AttendanceStatus;

public record SubAttendanceUpdateRequest(
	Long subAttendanceId,
	AttendanceStatus status
) {
}
