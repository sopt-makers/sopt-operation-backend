package org.sopt.makers.operation.dto.lecture;

import static org.sopt.makers.operation.entity.AttendanceStatus.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.lecture.Attribute;
import org.sopt.makers.operation.entity.lecture.Lecture;

public record LectureResponseDTO(
	String name,
	int generation,
	Part part,
	Attribute attribute,
	List<MemberVO> members
) {
	public static LectureResponseDTO of(Lecture lecture, List<Attendance> attendances) {

		List<MemberVO> members = attendances.stream().map(MemberVO::of).toList();

		return new LectureResponseDTO(
			lecture.getName(),
			lecture.getGeneration(),
			lecture.getPart(),
			lecture.getAttribute(),
			members
		);
	}
}

record MemberVO(
	String name,
	String university,
	List<MemberAttendanceVO> attendances,
	float score
) {
	static MemberVO of(Attendance attendance) {
		List<MemberAttendanceVO> attendances = attendance.getSubAttendances()
			.stream()
			.map(MemberAttendanceVO::of)
			.toList();

		float score = 0;
		if (attendance.getLecture().getEndDate().isBefore(LocalDateTime.now())) {
			score = getScore(attendance.getLecture().getAttribute(), attendances);
		}

		return new MemberVO(
			attendance.getMember().getName(),
			attendance.getMember().getUniversity(),
			attendances,
			score
		);
	}

	private static float getScore(Attribute attribute, List<MemberAttendanceVO> attendances) {
		switch (attribute) {
			case SEMINAR -> {
				return getResultInSeminar32(attendances.get(0).status(), attendances.get(1).status());
			}
			case EVENT -> {
				return getResultInEvent32(attendances.get(1).status());
			}
		}
		return 0;
	}

	private static float getResultInSeminar32 (AttendanceStatus first, AttendanceStatus second) {
		if (first.equals(ATTENDANCE) && second.equals(ATTENDANCE)) {
			return 0; // 출석
		} else if (first.equals(ABSENT) && second.equals(ATTENDANCE)) {
			return -0.5f; // 지각
		}
		return -1;
	}

	private static float getResultInEvent32(AttendanceStatus second) {
		return second.equals(ATTENDANCE) ? 0.5f : 0;
	}
}

record MemberAttendanceVO(
	int round,
	AttendanceStatus status,
	String date) {
	static MemberAttendanceVO of(SubAttendance subAttendance) {
		return new MemberAttendanceVO(
			subAttendance.getSubLecture().getRound(),
			subAttendance.getStatus(),
			subAttendance.getSubLecture().getStartAt() != null
				? convertDateToString(subAttendance.getLastModifiedDate())
				: null
		);
	}

	private static String convertDateToString(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
	}

}
