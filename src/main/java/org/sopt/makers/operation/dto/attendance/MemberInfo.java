package org.sopt.makers.operation.dto.attendance;

import java.time.LocalDateTime;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.lecture.Attribute;

import com.querydsl.core.annotations.QueryProjection;

public record MemberInfo(
	String lectureName,
	Attribute lectureAttribute,
	Long attendanceId,
	AttendanceStatus attendanceStatus,
	int round,
	AttendanceStatus subAttendanceStatus,
	LocalDateTime updatedAt
) {
	@QueryProjection
	public MemberInfo {
	}
}
