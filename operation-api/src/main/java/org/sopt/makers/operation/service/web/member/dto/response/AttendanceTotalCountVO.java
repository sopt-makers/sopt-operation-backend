package org.sopt.makers.operation.service.web.member.dto.response;

public record AttendanceTotalCountVO(
		int attendance,
		int absent,
		int tardy,
		int participate
) {
	public static AttendanceTotalCountVO of(int attendance, int absent, int tardy, int participate){
		return new AttendanceTotalCountVO(
				attendance,
				absent,
				tardy,
				participate
		);
	}
}
