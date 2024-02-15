package org.sopt.makers.operation.app.attendance.dto.response;

import lombok.Builder;
import org.sopt.makers.operation.attendance.domain.SubAttendance;

@Builder
public record LectureAttendResponse(
        Long subLectureId
) {
    public static LectureAttendResponse of(SubAttendance subAttendance) {
        return LectureAttendResponse.builder()
                .subLectureId(subAttendance.getSubLecture().getId())
                .build();
    }
}
