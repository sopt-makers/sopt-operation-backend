package org.sopt.makers.operation.service.app.lecture.dto.response;

import org.operation.lecture.SubLecture;

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
