package org.sopt.makers.operation.dto.member;

import java.util.List;

public record MembersResponseDTO(
	List<MemberListGetResponse> members,
	int totalCount
) {
	public static MembersResponseDTO of(List<MemberListGetResponse> members, int totalCount) {
		return new MembersResponseDTO(members, totalCount);
	}
}
