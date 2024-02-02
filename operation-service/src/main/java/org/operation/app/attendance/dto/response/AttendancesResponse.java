package org.operation.app.attendance.dto.response;

import java.util.List;

import org.operation.attendance.domain.Attendance;
import org.operation.web.attendance.dto.response.MemberResponse;
import org.operation.web.member.dto.response.MemberListGetResponse;

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
