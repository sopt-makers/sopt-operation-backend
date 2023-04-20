package org.sopt.makers.operation.dto.attendance;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.lecture.Attribute;

import com.querydsl.core.annotations.QueryProjection;

public record AttendanceInfo(Attribute attribute, AttendanceStatus status) {
	@QueryProjection
	public AttendanceInfo {
	}
}
