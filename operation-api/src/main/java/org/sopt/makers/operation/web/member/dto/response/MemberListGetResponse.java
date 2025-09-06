package org.sopt.makers.operation.web.member.dto.response;

import static lombok.AccessLevel.*;
import static org.sopt.makers.operation.attendance.domain.AttendanceStatus.*;

import java.util.List;

import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.member.domain.Member;

import lombok.Builder;

@Builder(access = PRIVATE)
public record MemberListGetResponse(
	List<MemberResponse> members,
	int totalCount
) {
	public static MemberListGetResponse of(List<Member> memberList, int totalCount) {
		return MemberListGetResponse.builder()
				.members(memberList.stream().map(MemberResponse::of).toList())
				.totalCount(totalCount)
				.build();
	}

	@Builder(access = PRIVATE)
	private record MemberResponse(
			long id,
			String name,
			Part part,
			float score,
			AttendanceStatusResponse total
	) {

		private static MemberResponse of(Member member) {
			return MemberResponse.builder()
					.id(member.getId())
					.name(member.getName())
					.part(member.getPart())
					.score(member.getScore())
					.total(AttendanceStatusResponse.of(member))
					.build();
		}
	}

	@Builder(access = PRIVATE)
	private record AttendanceStatusResponse(
			int attendance,
			int absent,
			int tardy,
			int participate
	) {

		private static AttendanceStatusResponse of(Member member) {
			return AttendanceStatusResponse.builder()
					.attendance(getCount(member, ATTENDANCE))
					.absent(getCount(member, ABSENT))
					.tardy(getCount(member, TARDY))
					.participate(getCount(member, PARTICIPATE))
					.build();
		}

		private static int getCount(Member member, AttendanceStatus status) {
			return (int)member.getAttendances().stream()
					.filter(attendance -> attendance.getStatus().equals(status))
					.count();
		}
	}
}
