package org.sopt.makers.operation.dto.lecture;

import org.sopt.makers.operation.entity.SubLecture;
import org.sopt.makers.operation.entity.lecture.Lecture;

public record AttendanceResponseDTO(
		Long lectureId,
		Long subLectureId
) {

	public static AttendanceResponseDTO of(Lecture lecture, SubLecture subLecture) {
		return new AttendanceResponseDTO(lecture.getId(), subLecture.getId());
	}
}
