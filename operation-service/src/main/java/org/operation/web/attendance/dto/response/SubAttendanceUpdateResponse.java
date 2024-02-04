package org.operation.web.attendance.dto.response;

import org.operation.attendance.domain.AttendanceStatus;
import org.operation.attendance.domain.SubAttendance;

public record SubAttendanceUpdateResponse(
	Long subAttendanceId,
	AttendanceStatus status
) {
	public static SubAttendanceUpdateResponse of(SubAttendance subAttendance) {
		return new SubAttendanceUpdateResponse(subAttendance.getId(), subAttendance.getStatus());
	}
}
