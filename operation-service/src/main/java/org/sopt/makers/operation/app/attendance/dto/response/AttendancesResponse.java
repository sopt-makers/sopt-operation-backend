package org.sopt.makers.operation.app.attendance.dto.response;

import java.util.List;

import org.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.web.attendance.dto.response.MemberResponse;

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
