package org.sopt.makers.operation.service.web.member.dto.response;

import java.util.List;

public record MembersResponse(
	List<MemberListGetResponse> members,
	int totalCount
) {
	public static MembersResponse of(List<MemberListGetResponse> members, int totalCount) {
		return new MembersResponse(members, totalCount);
	}

}
