package org.operation.web.lecture.dto.response;

import static org.operation.attendance.domain.AttendanceStatus.*;

import java.time.LocalDateTime;

import org.operation.attendance.domain.AttendanceStatus;
import org.operation.lecture.Lecture;

import lombok.Builder;

@Builder
public record AttendancesStatusVO(
	int attendance,
	int absent,
	int tardy,
	int unknown
) {
	public static AttendancesStatusVO of(Lecture lecture) {
		return AttendancesStatusVO.builder()
			.attendance(getCount(lecture, ATTENDANCE))
			.absent(isEnd(lecture) ? getCount(lecture, ABSENT) : 0)
			.tardy(getCount(lecture, TARDY))
			.unknown(isEnd(lecture) ? 0 : getCount(lecture, ABSENT))
			.build();
	}

	public static int getCount(Lecture lecture, AttendanceStatus status) {
		return (int)lecture.getAttendances().stream()
			.filter(attendance -> attendance.getStatus().equals(status))
			.count();
	}

	public static boolean isEnd(Lecture lecture) {
		return lecture.getEndDate().isBefore(LocalDateTime.now());
	}
}
