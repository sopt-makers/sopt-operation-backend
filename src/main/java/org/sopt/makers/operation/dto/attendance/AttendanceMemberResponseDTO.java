package org.sopt.makers.operation.dto.attendance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.lecture.Attribute;

public record AttendanceMemberResponseDTO(
	String name,
	float score,
	String part,
	String university,
	String phone,
	List<LectureVO> lectures
) {
	public static AttendanceMemberResponseDTO of(Member member) {
		return new AttendanceMemberResponseDTO(
			member.getName(),
			member.getScore(),
			member.getPart().getName(),
			member.getUniversity(),
			member.getPhone(),
			member.getAttendances().stream().map(LectureVO::of).toList()
		);
	}
}

record LectureVO(
	String lecture,
	float additiveScore,
	List<AttendanceVO> attendances
) {
	public static LectureVO of(Attendance attendance) {
		return new LectureVO(
			attendance.getLecture().getName(),
			getAdditiveScore(attendance),
			attendance.getSubAttendances().stream().map(AttendanceVO::of).toList()
		);
	}

	public static float getAdditiveScore(Attendance attendance) {
		if (attendance.getLecture().getAttribute().equals(Attribute.SEMINAR)) {
			if (attendance.getStatus().equals(AttendanceStatus.ABSENT)) {
				return -1;
			} else if (attendance.getStatus().equals(AttendanceStatus.TARDY)) {
				return -0.5f;
			}
			return 0;
		} else if (attendance.getLecture().getAttribute().equals(Attribute.EVENT)) {
			if (attendance.getStatus().equals(AttendanceStatus.ATTENDANCE)) {
				return 0.5f;
			}
			return 0f;
		}
		return 0f;
	}
}

record AttendanceVO(
	int round,
	String status,
	String date
) {
	public static AttendanceVO of(SubAttendance subAttendance) {
		return new AttendanceVO(
			subAttendance.getSubLecture().getRound(),
			subAttendance.getStatus().getName(),
			convertDate(subAttendance.getLastModifiedDate())
		);
	}

	private static String convertDate(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
	}
}
