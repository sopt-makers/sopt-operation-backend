package org.sopt.makers.operation.web.lecture.dto.response;

import static lombok.AccessLevel.*;

import org.sopt.makers.operation.lecture.domain.Lecture;

import lombok.Builder;

@Builder(access = PRIVATE)
public record LectureCreateResponse(
		long lectureId
) {

	public static LectureCreateResponse of(Lecture lecture) {
		return LectureCreateResponse.builder()
				.lectureId(lecture.getId())
				.build();
	}
}
