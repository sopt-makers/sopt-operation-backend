package org.sopt.makers.operation.dto.attendance;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.SubAttendance;

public record AttendUpdateResponseDTO(
	Long subAttendanceId,
	AttendanceStatus status
) {
	public static AttendUpdateResponseDTO of(SubAttendance subAttendance) {
		return new AttendUpdateResponseDTO(subAttendance.getId(), subAttendance.getStatus());
	}
}
