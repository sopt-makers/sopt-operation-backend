package org.sopt.makers.operation.web.attendnace.dto.response;

import java.util.List;

import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.attendance.domain.SubAttendance;
import org.sopt.makers.operation.member.domain.Member;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record AttendanceListResponse(
		List<AttendanceVO> attendances,
		int totalCount
) {

	public static AttendanceListResponse of(List<Attendance> attendanceList, int totalCount) {
		return AttendanceListResponse.builder()
				.attendances(attendanceList.stream().map(AttendanceVO::of).toList())
				.totalCount(totalCount)
				.build();
	}

	@Builder(access = AccessLevel.PRIVATE)
	private record AttendanceVO(
			long attendanceId,
			MemberVO member,
			List<SubAttendanceVO> attendances,
			float updatedScore
	) {
		private static AttendanceVO of(Attendance attendance) {
			return AttendanceVO.builder()
					.attendanceId(attendance.getId())
					.member(MemberVO.of(attendance.getMember()))
					.attendances(attendance.getSubAttendances().stream().map(SubAttendanceVO::of).toList())
					.updatedScore(attendance.getScore())
					.build();
		}
	}

	@Builder(access = AccessLevel.PRIVATE)
	private record MemberVO(
			long memberId,
			String name,
			String university,
			String part
	) {
		private static MemberVO of(Member member) {
			return MemberVO.builder()
					.memberId(member.getId())
					.name(member.getName())
					.university(member.getUniversity())
					.part(member.getPart().getName())
					.build();
		}
	}

	@Builder(access = AccessLevel.PRIVATE)
	private record SubAttendanceVO(
			long subAttendanceId,
			int round,
			AttendanceStatus status,
			String updateAt
	) {
		private static SubAttendanceVO of(SubAttendance subAttendance) {
			return SubAttendanceVO.builder()
					.subAttendanceId(subAttendance.getId())
					.round(subAttendance.getSubLecture().getRound())
					.status(subAttendance.getStatus())
					.updateAt(subAttendance.getLastModifiedDate().toString())
					.build();
		}
	}
}
