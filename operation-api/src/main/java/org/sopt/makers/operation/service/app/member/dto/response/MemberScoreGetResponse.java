package org.sopt.makers.operation.service.app.member.dto.response;

public record MemberScoreGetResponse(
		float score
) {
	public static MemberScoreGetResponse of(float score){
		return new MemberScoreGetResponse(
				score
		);
	}
}
