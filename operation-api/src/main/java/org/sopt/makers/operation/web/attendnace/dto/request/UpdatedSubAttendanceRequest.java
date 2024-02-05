package org.sopt.makers.operation.web.attendnace.dto.request;

import org.sopt.makers.operation.domain.attendance.domain.AttendanceStatus;

public record UpdatedSubAttendanceRequest(
	long subAttendanceId,
	AttendanceStatus status
) {
}
