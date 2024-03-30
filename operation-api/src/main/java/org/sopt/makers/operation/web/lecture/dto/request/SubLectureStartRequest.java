package org.sopt.makers.operation.web.lecture.dto.request;

import lombok.NonNull;

public record SubLectureStartRequest(
	long lectureId,
	int round,
	@NonNull String code
) {
}
