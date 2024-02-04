package org.sopt.makers.operation.service.web.lecture.dto.response;

import org.operation.lecture.Lecture;
import org.operation.lecture.SubLecture;

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
