package org.sopt.makers.operation.dto.attendance;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.lecture.Attribute;

public record AttendanceRequestDTO(
	Long subAttendanceId,
	AttendanceStatus status,
	Attribute attribute
) {
}
