package org.sopt.makers.operation.app.attendance.dto.response;

import static lombok.AccessLevel.*;

import lombok.Builder;

import org.sopt.makers.operation.attendance.domain.SubAttendance;

@Builder(access = PRIVATE)
public record LectureAttendResponse(
        long subLectureId
) {
    public static LectureAttendResponse of(SubAttendance subAttendance) {
        return LectureAttendResponse.builder()
                .subLectureId(subAttendance.getSubLecture().getId())
                .build();
    }
}
