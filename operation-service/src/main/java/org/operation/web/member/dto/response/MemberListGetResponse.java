package org.operation.web.member.dto.response;

import org.operation.common.domain.Part;
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
