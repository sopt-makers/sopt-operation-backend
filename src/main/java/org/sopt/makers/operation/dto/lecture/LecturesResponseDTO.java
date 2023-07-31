package org.sopt.makers.operation.dto.lecture;

import static org.sopt.makers.operation.entity.AttendanceStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.lecture.Lecture;

import lombok.*;

public record LecturesResponseDTO(
	int generation,
	List<LectureVO> lectures
) {

	public static LecturesResponseDTO of(int generation, List<Lecture> lectures) {
		return new LecturesResponseDTO(
			generation,
			lectures.stream().map(LectureVO::of).toList()
		);
	}
}

@Builder
record LectureVO(
	Long lectureId,
	String name,
	Part partValue,
	String partName,
	String startDate,
	Attribute attributeValue,
	String attributeName,
	AttendanceCountVO status
) {
	public static LectureVO of(Lecture lecture) {
		return LectureVO.builder()
			.lectureId(lecture.getId())
			.name(lecture.getName())
			.partValue(lecture.getPart())
			.partName(lecture.getPart().getName())
			.startDate(lecture.getStartDate().toString())
			.attributeValue(lecture.getAttribute())
			.attributeName(lecture.getAttribute().getName())
			.status(AttendanceCountVO.of(lecture))
			.build();
	}
}

@Builder
record AttendanceCountVO(
	int attendance,
	int absent,
	int tardy,
	int unknown
) {
	public static AttendanceCountVO of(Lecture lecture) {
		return AttendanceCountVO.builder()
			.attendance(getCount(lecture, ATTENDANCE))
			.absent(isEnd(lecture) ? getCount(lecture, ABSENT) : 0)
			.tardy(getCount(lecture, TARDY))
			.unknown(isEnd(lecture) ? 0 : getCount(lecture, ABSENT))
			.build();
	}

	private static int getCount(Lecture lecture, AttendanceStatus status) {
		return (int)lecture.getAttendances().stream()
			.filter(attendance -> attendance.getStatus().equals(status))
			.count();
	}

	private static boolean isEnd(Lecture lecture) {
		return lecture.getEndDate().isBefore(LocalDateTime.now());
	}
}

