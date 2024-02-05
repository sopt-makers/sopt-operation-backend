package org.sopt.makers.operation.web.attendnace.dto.response;

import java.util.List;

import org.sopt.makers.operation.domain.attendance.domain.Attendance;

public record AttendanceListResponse(
		List<MemberResponse> attendances,
		int totalCount
) {

	public static AttendanceListResponse of(List<Attendance> attendances, int totalCount) {
		return new AttendanceListResponse(
				attendances.stream().map(MemberResponse::of).toList(),
				totalCount);
	}
}
