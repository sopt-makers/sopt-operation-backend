package org.sopt.makers.operation.web.lecture.dto.response;

import static lombok.AccessLevel.*;

import org.sopt.makers.operation.lecture.domain.Lecture;

import lombok.Builder;

@Builder(access = PRIVATE)
public record LectureDetailGetResponse(
	long lectureId,
	String part,
	String name,
	String place,
	String attribute,
	String startDate,
	String endDate,
	int generation
) {

	public static LectureDetailGetResponse of(Lecture lecture) {
		return LectureDetailGetResponse.builder()
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
