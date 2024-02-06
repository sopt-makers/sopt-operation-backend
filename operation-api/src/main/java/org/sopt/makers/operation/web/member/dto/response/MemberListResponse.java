package org.sopt.makers.operation.web.member.dto.response;

import static org.sopt.makers.operation.attendance.domain.AttendanceStatus.*;

import java.util.List;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.member.domain.Member;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MemberListResponse(
	List<MemberVO> members,
	int totalCount
) {
	public static MemberListResponse of(List<Member> memberList, int totalCount) {
		return MemberListResponse.builder()
				.members(memberList.stream().map(MemberVO::of).toList())
				.totalCount(totalCount)
				.build();
	}

	@Builder(access = AccessLevel.PRIVATE)
	private record MemberVO(
			Long id,
			String name,
			String university,
			Part part,
			float score,
			AttendanceInfo total
	) {

		private static MemberVO of(Member member) {
			return MemberVO.builder()
					.id(member.getId())
					.name(member.getName())
					.university(member.getUniversity())
					.part(member.getPart())
					.score(member.getScore())
					.total(AttendanceInfo.of(member))
					.build();
		}
	}

	@Builder(access = AccessLevel.PRIVATE)
	private record AttendanceInfo(
			int attendance,
			int absent,
			int tardy,
			int participate
	) {

		private static AttendanceInfo of(Member member) {
			return AttendanceInfo.builder()
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
