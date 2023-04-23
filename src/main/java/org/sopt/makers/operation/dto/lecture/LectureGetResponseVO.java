package org.sopt.makers.operation.dto.lecture;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.lecture.Lecture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record LectureGetResponseVO(
        AttendanceStatus status,
        String attendedAt

) {
    public static LectureGetResponseVO of(AttendanceStatus status, LocalDateTime attendedAt){
        return new LectureGetResponseVO(
                status,
                attendedAt.format(convertFormat())
        );
    }

    private static DateTimeFormatter convertFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    }
}
