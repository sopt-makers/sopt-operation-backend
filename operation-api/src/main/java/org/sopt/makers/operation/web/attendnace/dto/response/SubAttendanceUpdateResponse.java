package org.sopt.makers.operation.web.attendnace.dto.response;

import static lombok.AccessLevel.*;

import org.sopt.makers.operation.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.attendance.domain.SubAttendance;

import lombok.Builder;

@Builder(access = PRIVATE)
public record SubAttendanceUpdateResponse(
	long subAttendanceId,
	AttendanceStatus status
) {

	public static SubAttendanceUpdateResponse of(SubAttendance subAttendance) {
		return SubAttendanceUpdateResponse.builder()
				.subAttendanceId(subAttendance.getId())
				.status(subAttendance.getStatus())
				.build();
	}
}
