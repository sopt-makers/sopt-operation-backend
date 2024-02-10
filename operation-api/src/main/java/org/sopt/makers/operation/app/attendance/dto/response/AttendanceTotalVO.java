package org.sopt.makers.operation.app.attendance.dto.response;

import java.time.format.DateTimeFormatter;

import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.lecture.domain.Attribute;

public record AttendanceTotalVO(
        Attribute attribute,
        String name,
        AttendanceStatus status,
        String date
) {
    public static AttendanceTotalVO of(Attendance attendance){
        return new AttendanceTotalVO(
                attendance.getLecture().getAttribute(),
                attendance.getLecture().getName(),
                attendance.getStatus(),
                attendance.getLecture().getStartDate()
                        .format(DateTimeFormatter.ofPattern("M월 d일"))
        );
    }

    public static AttendanceTotalVO getTotalAttendanceVO(Attendance attendance) {
        return AttendanceTotalVO.of(attendance);
    }

    public static AttendanceStatus getAttendanceStatus(AttendanceTotalVO attendance) {
        return attendance.status();
    }
}
