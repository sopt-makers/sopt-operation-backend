package org.operation.web.attendance.dto.response;

import java.util.List;

import org.operation.attendance.domain.Attendance;
import org.operation.attendance.domain.SubAttendance;
import org.operation.member.domain.Member;

public record AttendanceMemberResponse(
	String name,
	float score,
	String part,
	String university,
	String phone,
	List<LectureVO> lectures
) {
	public static AttendanceMemberResponse of(Member member, List<Attendance> attendances) {
		return new AttendanceMemberResponse(
			member.getName(),
			member.getScore(),
			member.getPart().getName(),
			member.getUniversity(),
			member.getPhone(),
			attendances.stream().map(LectureVO::of).toList());
	}

	record LectureVO(
			String lecture,
			float additiveScore,
			String status,
			List<AttendanceVO> attendances
	) {
		public static LectureVO of(Attendance attendance) {
			return new LectureVO(
					attendance.getLecture().getName(),
					attendance.getScore(),
					attendance.getStatus().getName(),
					attendance.getSubAttendances().stream().map(AttendanceVO::of).toList());
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
					subAttendance.getLastModifiedDate().toString()
			);
		}
	}
}
