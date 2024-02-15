package org.sopt.makers.operation.app.attendance.dto.request;

import lombok.NonNull;

public record LectureAttendRequest(
        long subLectureId,
        @NonNull String code
) {
}
