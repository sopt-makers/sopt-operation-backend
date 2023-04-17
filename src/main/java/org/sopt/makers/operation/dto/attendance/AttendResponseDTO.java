package org.sopt.makers.operation.dto.attendance;

import org.sopt.makers.operation.entity.Attendance;

import java.time.format.DateTimeFormatter;

public record AttendResponseDTO(
        Long subLectureId
) {
    public static AttendResponseDTO of(Long subLectureId){
        return new AttendResponseDTO(
                subLectureId
        );
    }
}
