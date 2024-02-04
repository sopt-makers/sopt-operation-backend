package org.sopt.makers.operation.service.web.lecture.dto.response;

import org.operation.lecture.Lecture;

import lombok.Builder;

@Builder
public record LectureDetailResponse(
	Long lectureId,
	String part,
	String name,
	String place,
	String attribute,
	String startDate,
	String endDate,
	int generation
) {
	public static LectureDetailResponse of(Lecture lecture) {
		return LectureDetailResponse.builder()
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
