package org.operation.app.member.dto.response;

public record MemberScoreGetResponse(
		float score
) {
	public static MemberScoreGetResponse of(float score){
		return new MemberScoreGetResponse(
				score
		);
	}
}
