package org.sopt.makers.operation.dto.attendance.response;

import java.util.List;

import org.sopt.makers.operation.entity.attendance.Attendance;
import org.sopt.makers.operation.entity.attendance.AttendanceStatus;
import org.sopt.makers.operation.entity.member.Member;
import org.sopt.makers.operation.entity.attendance.SubAttendance;

public record MemberResponseDTO (
	Long attendanceId,
	MemberVO member,
	List<SubAttendanceVO> attendances,
	float updatedScore
) {

	public static MemberResponseDTO of(Attendance attendance) {
		return new MemberResponseDTO(
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
