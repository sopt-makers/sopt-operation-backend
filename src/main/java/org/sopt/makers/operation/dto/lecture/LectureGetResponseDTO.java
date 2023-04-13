package org.sopt.makers.operation.dto.lecture;

import org.sopt.makers.operation.entity.lecture.Lecture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record LectureGetResponseDTO(
        LectureResponseType type,
        Long id,
        String location,
        String name,
        String startDate,
        String endDate,
        String message,
        List<LectureGetResponseVO> attendances
) {
    public static LectureGetResponseDTO of(LectureResponseType type, Lecture lecture, String message, List<LectureGetResponseVO> attendances) {

        return new LectureGetResponseDTO(
                type,
                lecture.getId(),
                lecture.getPlace(),
                lecture.getName(),
                lecture.getStartDate().format(convertFormat()),
                lecture.getEndDate().format(convertFormat()),
                message,
                attendances
        );
    }

    private static DateTimeFormatter convertFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    }
}
