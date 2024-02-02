package org.operation.app.attendance.dto.request;

public record AttendanceRequest(
        Long subLectureId,
        String code
) {
}
