package org.sopt.makers.operation.web.lecture.dto.request;

public record SubLectureStartRequest(
	long lectureId,
	int round,
	String code
) {
}
