package org.sopt.makers.operation.dto.attendance;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.SubAttendance;

public record SubAttendanceUpdateResponseDTO(
	Long subAttendanceId,
	AttendanceStatus status
) {
	public static SubAttendanceUpdateResponseDTO of(SubAttendance subAttendance) {
		return new SubAttendanceUpdateResponseDTO(subAttendance.getId(), subAttendance.getStatus());
	}
}
