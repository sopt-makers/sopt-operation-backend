package org.sopt.makers.operation.dto.lecture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Lecture;

public record LectureVO(String name, Part part, String date, AttendanceVO status) {

	public static LectureVO of(Lecture lecture, AttendanceVO status) {
		return new LectureVO(
			lecture.getName(),
			lecture.getPart(),
			convertDateToString(lecture.getStartDate()),
			status
		);
	}

	private static String convertDateToString(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
	}
}
