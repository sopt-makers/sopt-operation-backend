package org.sopt.makers.operation.dto.lecture;

import org.sopt.makers.operation.entity.SubLecture;

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
