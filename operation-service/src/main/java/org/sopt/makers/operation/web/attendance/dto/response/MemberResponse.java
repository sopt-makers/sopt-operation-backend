package org.sopt.makers.operation.web.attendance.dto.response;

import java.util.List;

import org.operation.attendance.domain.Attendance;
import org.operation.attendance.domain.AttendanceStatus;
import org.operation.attendance.domain.SubAttendance;
import org.operation.member.domain.Member;

public record MemberResponse(
	Long attendanceId,
	MemberVO member,
	List<SubAttendanceVO> attendances,
	float updatedScore
) {

	public static MemberResponse of(Attendance attendance) {
		return new MemberResponse(
			attendance.getId(),
			MemberVO.of(attendance.getMember()),
			attendance.getSubAttendances().stream().map(SubAttendanceVO::of).toList(),
			attendance.getScore());
	}

	record MemberVO(Long memberId, String name, String university, String part) {
		static MemberVO of(Member member) {
			return new MemberVO(
					member.getId(),
					member.getName(),
					member.getUniversity(),
					member.getPart().getName());
		}
	}

	record SubAttendanceVO(Long subAttendanceId, int round, AttendanceStatus status, String updateAt) {
		static SubAttendanceVO of(SubAttendance subAttendance) {
			return new SubAttendanceVO(
					subAttendance.getId(),
					subAttendance.getSubLecture().getRound(),
					subAttendance.getStatus(),
					subAttendance.getLastModifiedDate().toString());
		}
	}
}
