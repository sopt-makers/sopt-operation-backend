package org.sopt.makers.operation.dto.attendance;

import java.util.List;

import org.sopt.makers.operation.entity.attendance.Attendance;

public record AttendancesResponseDTO(
	List<MemberResponseDTO> attendances,
	int totalCount
) {
	public static AttendancesResponseDTO of(List<Attendance> attendances, int totalCount) {
		return new AttendancesResponseDTO(
			attendances.stream().map(MemberResponseDTO::of).toList(),
			totalCount);
	}
}
