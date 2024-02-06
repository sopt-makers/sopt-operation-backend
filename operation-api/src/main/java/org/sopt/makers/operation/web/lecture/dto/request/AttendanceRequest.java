package org.sopt.makers.operation.web.lecture.dto.request;

public record AttendanceRequest(
	long lectureId,
	int round,
	String code
) {
}
