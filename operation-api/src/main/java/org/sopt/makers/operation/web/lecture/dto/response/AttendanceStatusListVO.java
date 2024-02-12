package org.sopt.makers.operation.web.lecture.dto.response;

import static org.sopt.makers.operation.attendance.domain.AttendanceStatus.*;

import org.sopt.makers.operation.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.lecture.domain.Lecture;

import lombok.Builder;

@Builder
public record AttendanceStatusListVO(
	int attendance,
	int absent,
	int tardy,
	int unknown
) {

	public static AttendanceStatusListVO of(Lecture lecture) {
		return AttendanceStatusListVO.builder()
			.attendance(getCount(lecture, ATTENDANCE))
			.absent(getAbsentCount(lecture))
			.tardy(getCount(lecture, TARDY))
			.unknown(getUnknownCount(lecture))
			.build();
	}

	private static int getCount(Lecture lecture, AttendanceStatus status) {
		return (int)lecture.getAttendances().stream()
			.filter(attendance -> attendance.getStatus().equals(status))
			.count();
	}

	private static int getAbsentCount(Lecture lecture) {
		return lecture.isEnd() ? getCount(lecture, ABSENT) : 0;
	}

	private static int getUnknownCount(Lecture lecture) {
		return lecture.isEnd() ? 0 : getCount(lecture, ABSENT);
	}
}
