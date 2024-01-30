package org.sopt.makers.operation.dto.lecture.request;

public record AttendanceRequestDTO(
	Long lectureId,
	int round,
	String code
) {
}
