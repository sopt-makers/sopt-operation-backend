package org.sopt.makers.operation.dto.lecture;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.SubLecture;

import java.time.LocalDateTime;

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
