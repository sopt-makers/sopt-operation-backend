package org.sopt.makers.operation.service.web.lecture.dto.request;

public record AttendanceRequest(
	Long lectureId,
	int round,
	String code
) {
}
