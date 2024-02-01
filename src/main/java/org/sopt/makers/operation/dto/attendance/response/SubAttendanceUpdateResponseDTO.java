package org.sopt.makers.operation.dto.attendance.response;

import org.operation.attendance.AttendanceStatus;
import org.operation.attendance.SubAttendance;

public record SubAttendanceUpdateResponseDTO(
	Long subAttendanceId,
	AttendanceStatus status
) {
	public static SubAttendanceUpdateResponseDTO of(SubAttendance subAttendance) {
		return new SubAttendanceUpdateResponseDTO(subAttendance.getId(), subAttendance.getStatus());
	}
}
