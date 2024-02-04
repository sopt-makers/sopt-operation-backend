package org.sopt.makers.operation.service.web.member.dto.response;

import org.sopt.makers.operation.domain.Part;
import org.operation.member.domain.Member;

public record MemberListGetResponse(
		Long id,
		String name,
		String university,
		Part part,
		float score,
		AttendanceTotalCountVO total
) {
	public static MemberListGetResponse of(Member member, AttendanceTotalCountVO total){
		return new MemberListGetResponse(
				member.getId(),
				member.getName(),
				member.getUniversity(),
				member.getPart(),
				member.getScore(),
				total
		);
	}
}
