package org.sopt.makers.operation.dto.lecture;

import java.util.List;

public record LecturesResponseDTO(
	int generation,
	List<LectureVO> lectures
) {

	public static LecturesResponseDTO of(int generation, List<LectureVO> lectures) {
		return new LecturesResponseDTO(generation, lectures);
	}
}

