package org.sopt.makers.operation.web.attendnace.dto.response;

import org.sopt.makers.operation.domain.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.domain.attendance.domain.SubAttendance;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UpdatedSubAttendanceResponse(
	long subAttendanceId,
	AttendanceStatus status
) {
	public static UpdatedSubAttendanceResponse of(SubAttendance subAttendance) {
		return UpdatedSubAttendanceResponse.builder()
				.subAttendanceId(subAttendance.getId())
				.status(subAttendance.getStatus())
				.build();
	}
}
