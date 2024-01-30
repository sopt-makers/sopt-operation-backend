package org.sopt.makers.operation.dto.lecture.response;

import org.sopt.makers.operation.entity.lecture.SubLecture;

public record LectureCurrentRoundResponseDTO(
        Long id,
        int round
) {
    public static LectureCurrentRoundResponseDTO of(SubLecture subLecture){
        return new LectureCurrentRoundResponseDTO(
                subLecture.getId(),
                subLecture.getRound()
        );
    }
}
