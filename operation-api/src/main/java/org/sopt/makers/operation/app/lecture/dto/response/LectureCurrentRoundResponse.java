package org.sopt.makers.operation.app.lecture.dto.response;

import org.sopt.makers.operation.domain.lecture.SubLecture;

public record LectureCurrentRoundResponse(
        Long id,
        int round
) {
    public static LectureCurrentRoundResponse of(SubLecture subLecture){
        return new LectureCurrentRoundResponse(
                subLecture.getId(),
                subLecture.getRound()
        );
    }
}
