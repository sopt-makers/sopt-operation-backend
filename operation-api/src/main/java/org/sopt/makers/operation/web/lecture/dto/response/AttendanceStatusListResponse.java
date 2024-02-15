package org.sopt.makers.operation.web.lecture.dto.response;

import static lombok.AccessLevel.*;
import static org.sopt.makers.operation.attendance.domain.AttendanceStatus.*;

import org.sopt.makers.operation.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.lecture.domain.Lecture;

import lombok.Builder;

@Builder(access = PRIVATE)
public record AttendanceStatusListResponse(
	int attendance,
	int absent,
	int tardy,
	int unknown
) {

	public static AttendanceStatusListResponse of(Lecture lecture) {
		return AttendanceStatusListResponse.builder()
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
