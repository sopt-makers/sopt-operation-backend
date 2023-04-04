package org.sopt.makers.operation.dto.lecture;

import java.util.List;

public record LectureResponseDTO(
	int generation,
	List<LectureVO> lectures
) {

	public static LectureResponseDTO of(int generation, List<LectureVO> lectures) {
		return new LectureResponseDTO(generation, lectures);
	}
}

