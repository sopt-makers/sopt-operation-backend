package org.sopt.makers.operation.web.lecture.dto.response;

import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.domain.SubLecture;

import lombok.Builder;

@Builder
public record AttendanceResponse(
		Long lectureId,
		Long subLectureId
) {

	public static AttendanceResponse of(Lecture lecture, SubLecture subLecture) {
		return AttendanceResponse.builder()
				.lectureId(lecture.getId())
				.subLectureId(subLecture.getId())
				.build();
	}
}
