package org.sopt.makers.operation.app.lecture.dto.response;

import lombok.Builder;
import org.sopt.makers.operation.lecture.domain.SubLecture;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record LectureCurrentRoundResponse(
        long id,
        int round
) {
    public static LectureCurrentRoundResponse of(SubLecture subLecture){
        return LectureCurrentRoundResponse.builder()
                .id(subLecture.getId())
                .round(subLecture.getRound())
                .build();
    }
}
