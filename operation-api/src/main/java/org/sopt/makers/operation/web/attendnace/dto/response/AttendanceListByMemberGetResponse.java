package org.sopt.makers.operation.web.attendnace.dto.response;

import static lombok.AccessLevel.*;

import java.util.List;

import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.attendance.domain.SubAttendance;
import org.sopt.makers.operation.member.domain.Member;

import lombok.Builder;

@Builder(access = PRIVATE)
public record AttendanceListByMemberGetResponse(
	String name,
	float score,
	String part,
	String university,
	String phone,
	List<LectureResponse> lectures
) {

	public static AttendanceListByMemberGetResponse of(Member member, List<Attendance> attendances) {
		return AttendanceListByMemberGetResponse.builder()
				.name(member.getName())
				.score(member.getScore())
				.part(member.getPart().getName())
				.university(member.getUniversity())
				.phone(member.getPhone())
				.lectures(attendances.stream().map(LectureResponse::of).toList())
				.build();
	}

	@Builder(access = PRIVATE)
	record LectureResponse(
			String lecture,
			float additiveScore,
			String status,
			List<AttendanceResponse> attendances
	) {

		public static LectureResponse of(Attendance attendance) {
			return LectureResponse.builder()
					.lecture(attendance.getLecture().getName())
					.additiveScore(attendance.getScore())
					.status(attendance.getStatus().getName())
					.attendances(attendance.getSubAttendances().stream().map(AttendanceResponse::of).toList())
					.build();
		}
	}

	@Builder(access = PRIVATE)
	record AttendanceResponse(
			int round,
			String status,
			String date
	) {

		public static AttendanceResponse of(SubAttendance subAttendance) {
			return AttendanceResponse.builder()
					.round(subAttendance.getSubLecture().getRound())
					.status(subAttendance.getStatus().getName())
					.date(subAttendance.getLastModifiedDate().toString())
					.build();
		}
	}
}
