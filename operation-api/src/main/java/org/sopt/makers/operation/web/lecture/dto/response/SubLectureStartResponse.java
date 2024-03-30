package org.sopt.makers.operation.web.lecture.dto.response;

import static lombok.AccessLevel.*;

import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.domain.SubLecture;

import lombok.Builder;

@Builder(access = PRIVATE)
public record SubLectureStartResponse(
		long lectureId,
		long subLectureId
) {

	public static SubLectureStartResponse of(Lecture lecture, SubLecture subLecture) {
		return SubLectureStartResponse.builder()
				.lectureId(lecture.getId())
				.subLectureId(subLecture.getId())
				.build();
	}
}
