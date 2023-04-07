package org.sopt.makers.operation.dto.member;

import org.sopt.makers.operation.dto.lecture.LectureGetResponseVO;
import org.sopt.makers.operation.entity.AttendanceStatus;

import java.time.LocalDateTime;

public record MemberScoreGetResponse(
        float score
) {
    public static MemberScoreGetResponse of(float score){
        return new MemberScoreGetResponse(
                score
        );
    }
}
