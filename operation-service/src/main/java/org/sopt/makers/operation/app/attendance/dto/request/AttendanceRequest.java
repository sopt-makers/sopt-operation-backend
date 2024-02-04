package org.sopt.makers.operation.app.attendance.dto.request;

public record AttendanceRequest(
        Long subLectureId,
        String code
) {
}
