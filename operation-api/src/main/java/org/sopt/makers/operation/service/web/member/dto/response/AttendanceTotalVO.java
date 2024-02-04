package org.sopt.makers.operation.service.web.member.dto.response;

import java.time.format.DateTimeFormatter;

import org.operation.attendance.domain.Attendance;
import org.operation.attendance.domain.AttendanceStatus;
import org.operation.lecture.Attribute;

public record AttendanceTotalVO(
		Attribute attribute,
		String name,
		AttendanceStatus status,
		String date
) {
	public static AttendanceTotalVO of(Attendance attendance){
		return new AttendanceTotalVO(
				attendance.getLecture().getAttribute(),
				attendance.getLecture().getName(),
				attendance.getStatus(),
				attendance.getLecture().getStartDate()
						.format(DateTimeFormatter.ofPattern("M월 d일"))
		);
	}

	public static AttendanceTotalVO getTotalAttendanceVO(Attendance attendance) {
		return AttendanceTotalVO.of(attendance);
	}

	public static AttendanceStatus getAttendanceStatus(AttendanceTotalVO attendance) {
		return attendance.status();
	}
}
