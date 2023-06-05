package org.sopt.makers.operation.dto.member;

public record MemberScoreGetResponse(
        float score
) {
    public static MemberScoreGetResponse of(float score){
        return new MemberScoreGetResponse(
                score
        );
    }
}
