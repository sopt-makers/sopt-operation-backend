package org.sopt.makers.operation.web.attendnace.dto.response;

import static lombok.AccessLevel.*;

import org.sopt.makers.operation.member.domain.Member;

import lombok.Builder;

@Builder(access = PRIVATE)
public record MemberScoreUpdateResponse(
		float score
) {

	public static MemberScoreUpdateResponse of(Member member) {
		return MemberScoreUpdateResponse.builder()
				.score(member.getScore())
				.build();
	}
}
