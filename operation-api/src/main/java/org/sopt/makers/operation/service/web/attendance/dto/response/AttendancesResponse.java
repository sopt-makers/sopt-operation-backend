package org.sopt.makers.operation.service.web.attendance.dto.response;

import java.util.List;

import org.operation.attendance.domain.Attendance;

public record AttendancesResponse(
		List<MemberResponse> attendances,
		int totalCount
) {

	public static AttendancesResponse of(List<Attendance> attendances, int totalCount) {
		return new AttendancesResponse(
				attendances.stream().map(MemberResponse::of).toList(),
				totalCount);
	}
}
