package org.sopt.makers.operation.dto.attendance;

import java.time.LocalDateTime;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.lecture.Attribute;

import com.querydsl.core.annotations.QueryProjection;

public record LectureInfo(Long attendanceId, Long memberId, String memberName, String university, Attribute attribute,
						  AttendanceStatus attendanceStatus, Long subAttendanceId, int round,
						  AttendanceStatus subAttendanceStatus, LocalDateTime updatedAt) {
	@QueryProjection
	public LectureInfo {
	}
}
