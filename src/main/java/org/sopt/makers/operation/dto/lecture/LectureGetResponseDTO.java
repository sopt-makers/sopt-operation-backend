package org.sopt.makers.operation.dto.lecture;

import org.sopt.makers.operation.entity.lecture.Lecture;

import java.time.LocalDateTime;
import java.util.List;

public record LectureGetResponseDTO(
        LectureResponseType type,
        String location,
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<LectureGetResponseVO> attendances
) {
    public static LectureGetResponseDTO of(LectureResponseType type, Lecture lecture, List<LectureGetResponseVO> attendances) {

        return new LectureGetResponseDTO(
                type,
                lecture.getPlace(),
                lecture.getName(),
                lecture.getStartDate(),
                lecture.getEndDate(),
                attendances
        );
    }
}
