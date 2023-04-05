package org.sopt.makers.operation.dto.lecture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.lecture.Lecture;

public record LectureVO(
	String name,
	Part partValue,
	String partName,
	String date,
	Attribute attributeValue,
	String attributeName,
	AttendanceVO status
) {

	public static LectureVO of(Lecture lecture, AttendanceVO status) {
		return new LectureVO(
			lecture.getName(),
			lecture.getPart(),
			lecture.getPart().getName(),
			convertDateToString(lecture.getStartDate()),
			lecture.getAttribute(),
			lecture.getAttribute().getName(),
			status
		);
	}

	private static String convertDateToString(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
	}
}
