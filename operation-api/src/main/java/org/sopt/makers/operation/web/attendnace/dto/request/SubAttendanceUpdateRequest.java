package org.sopt.makers.operation.web.attendnace.dto.request;

import org.sopt.makers.operation.attendance.domain.AttendanceStatus;

public record SubAttendanceUpdateRequest(
	long subAttendanceId,
	AttendanceStatus status
) {
}
