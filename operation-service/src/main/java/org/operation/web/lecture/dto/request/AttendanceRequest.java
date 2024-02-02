package org.operation.web.lecture.dto.request;

public record AttendanceRequest(
	Long lectureId,
	int round,
	String code
) {
}
