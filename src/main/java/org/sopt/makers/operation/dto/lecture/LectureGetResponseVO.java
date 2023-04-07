package org.sopt.makers.operation.dto.lecture;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.lecture.Lecture;

import java.time.LocalDateTime;
import java.util.List;

public record LectureGetResponseVO(
        AttendanceStatus status,
        LocalDateTime attendedAt
) {
    public static LectureGetResponseVO of(AttendanceStatus status, LocalDateTime attendedAt){
        return new LectureGetResponseVO(
                status,
                attendedAt
        );
    }
}
