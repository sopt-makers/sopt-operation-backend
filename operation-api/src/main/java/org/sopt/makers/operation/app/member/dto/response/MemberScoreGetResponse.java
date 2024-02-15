package org.sopt.makers.operation.app.member.dto.response;

import static lombok.AccessLevel.PRIVATE;

import lombok.Builder;

@Builder(access = PRIVATE)
public record MemberScoreGetResponse(
		float score
) {
	public static MemberScoreGetResponse of(float score){
		return MemberScoreGetResponse.builder()
				.score(score)
				.build();
	}
}
