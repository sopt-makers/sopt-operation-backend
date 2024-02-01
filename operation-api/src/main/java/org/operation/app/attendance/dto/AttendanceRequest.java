package org.operation.app.attendance.dto;

public record AttendanceRequest(
        Long subLectureId,
        String code
) {
}
