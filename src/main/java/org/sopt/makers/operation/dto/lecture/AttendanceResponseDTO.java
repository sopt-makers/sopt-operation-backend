package org.sopt.makers.operation.dto.lecture;

import org.sopt.makers.operation.entity.lecture.SubLecture;
import org.sopt.makers.operation.entity.lecture.Lecture;

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
