package org.sopt.makers.operation.dto.lecture.response;

import org.operation.lecture.Lecture;

import lombok.Builder;

@Builder
public record LectureDetailResponseDTO(
	Long lectureId,
	String part,
	String name,
	String place,
	String attribute,
	String startDate,
	String endDate,
	int generation
) {
	public static LectureDetailResponseDTO of(Lecture lecture) {
		return LectureDetailResponseDTO.builder()
			.lectureId(lecture.getId())
			.part(lecture.getPart().getName())
			.name(lecture.getName())
			.place(lecture.getPlace())
			.attribute(lecture.getAttribute().getName())
			.startDate(lecture.getStartDate().toString())
			.endDate(lecture.getEndDate().toString())
			.generation(lecture.getGeneration())
			.build();
	}
}
