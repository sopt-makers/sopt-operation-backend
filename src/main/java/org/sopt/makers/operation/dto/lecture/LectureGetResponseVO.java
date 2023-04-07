package org.sopt.makers.operation.dto.lecture;

import org.sopt.makers.operation.entity.AttendanceStatus;

import java.time.LocalDateTime;

public record LectureGetResponseVO(
        AttendanceStatus status,
        LocalDateTime attendedAt
) {
}
