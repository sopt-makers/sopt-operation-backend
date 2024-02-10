package org.sopt.makers.operation.app.member.dto.response;

import static org.sopt.makers.operation.attendance.domain.AttendanceStatus.*;

import org.sopt.makers.operation.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.member.domain.Member;

import lombok.Builder;

@Builder
public record AttendanceStatusListVO(
		int attendance,
		int absent,
		int tardy,
		int participate
) {

	public static AttendanceStatusListVO of(Member member) {
		return AttendanceStatusListVO.builder()
			.attendance(getCount(member, ATTENDANCE))
			.absent(getCount(member, ABSENT))
			.tardy(getCount(member, TARDY))
			.participate(getCount(member, PARTICIPATE))
			.build();
	}

	private static int getCount(Member member, AttendanceStatus status) {
		return (int)member.getAttendances().stream()
			.filter(attendance -> attendance.getStatus().equals(status))
			.count();
	}
}
