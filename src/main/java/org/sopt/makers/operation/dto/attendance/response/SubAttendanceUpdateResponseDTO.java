package org.sopt.makers.operation.dto.attendance.response;

import org.sopt.makers.operation.entity.attendance.AttendanceStatus;
import org.sopt.makers.operation.entity.attendance.SubAttendance;

public record SubAttendanceUpdateResponseDTO(
	Long subAttendanceId,
	AttendanceStatus status
) {
	public static SubAttendanceUpdateResponseDTO of(SubAttendance subAttendance) {
		return new SubAttendanceUpdateResponseDTO(subAttendance.getId(), subAttendance.getStatus());
	}
}
