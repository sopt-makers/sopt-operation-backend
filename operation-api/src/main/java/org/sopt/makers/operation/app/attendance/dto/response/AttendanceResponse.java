package org.sopt.makers.operation.app.attendance.dto.response;

import org.sopt.makers.operation.attendance.domain.SubAttendance;

public record AttendanceResponse(
        Long subLectureId
) {

    public static AttendanceResponse of(SubAttendance subAttendance) {
        return new AttendanceResponse(subAttendance.getSubLecture().getId());
    }
}
