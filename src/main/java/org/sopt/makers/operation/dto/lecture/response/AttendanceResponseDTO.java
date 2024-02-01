package org.sopt.makers.operation.dto.lecture.response;

import org.operation.lecture.SubLecture;
import org.operation.lecture.Lecture;

import lombok.Builder;

@Builder
public record AttendanceResponseDTO(
		Long lectureId,
		Long subLectureId
) {

	public static AttendanceResponseDTO of(Lecture lecture, SubLecture subLecture) {
		return AttendanceResponseDTO.builder()
				.lectureId(lecture.getId())
				.subLectureId(subLecture.getId())
				.build();
	}
}
