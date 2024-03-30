package org.sopt.makers.operation.web.attendnace.dto.response;

import static lombok.AccessLevel.*;

import java.util.List;

import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.attendance.domain.SubAttendance;
import org.sopt.makers.operation.member.domain.Member;

import lombok.Builder;

@Builder(access = PRIVATE)
public record AttendanceListByLectureGetResponse(
		List<AttendanceResponse> attendances,
		int totalCount
) {

	public static AttendanceListByLectureGetResponse of(List<Attendance> attendanceList, int totalCount) {
		return AttendanceListByLectureGetResponse.builder()
				.attendances(attendanceList.stream().map(AttendanceResponse::of).toList())
				.totalCount(totalCount)
				.build();
	}

	@Builder(access = PRIVATE)
	private record AttendanceResponse(
			long attendanceId,
			MemberResponse member,
			List<SubAttendanceVO> attendances,
			float updatedScore
	) {
		private static AttendanceResponse of(Attendance attendance) {
			return AttendanceResponse.builder()
					.attendanceId(attendance.getId())
					.member(MemberResponse.of(attendance.getMember()))
					.attendances(attendance.getSubAttendances().stream().map(SubAttendanceVO::of).toList())
					.updatedScore(attendance.getScore())
					.build();
		}
	}

	@Builder(access = PRIVATE)
	private record MemberResponse(
			long memberId,
			String name,
			String university,
			String part
	) {
		private static MemberResponse of(Member member) {
			return MemberResponse.builder()
					.memberId(member.getId())
					.name(member.getName())
					.university(member.getUniversity())
					.part(member.getPart().getName())
					.build();
		}
	}

	@Builder(access = PRIVATE)
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
