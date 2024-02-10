package org.sopt.makers.operation.web.attendnace.dto.request;

import org.sopt.makers.operation.attendance.domain.AttendanceStatus;

public record UpdatedSubAttendanceRequest(
	long subAttendanceId,
	AttendanceStatus status
) {
}
