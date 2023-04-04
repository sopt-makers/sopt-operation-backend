package org.sopt.makers.operation.dto.lecture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.lecture.Lecture;

public record LectureResponseDTO(
	String name,
	int generation,
	Part part,
	List<MemberVO> members
) {
	public static LectureResponseDTO of(Lecture lecture, List<Attendance> attendances) {
		return new LectureResponseDTO(
			lecture.getName(),
			lecture.getGeneration(),
			lecture.getPart(),
			attendances.stream().map(MemberVO::of).toList()
		);
	}
}

record MemberVO(
	String name,
	String university,
	List<MemberAttendanceVO> attendances
) {
	static MemberVO of(Attendance attendance) {
		return new MemberVO(
			attendance.getMember().getName(),
			attendance.getMember().getUniversity(),
			attendance.getSubAttendances().stream().map(MemberAttendanceVO::of).toList()
		);
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
			convertDateToString(subAttendance.getLastModifiedDate())
		);
	}

	private static String convertDateToString(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
	}

}
