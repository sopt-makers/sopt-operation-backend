package org.sopt.makers.operation.dto.attendance.response;

import org.operation.attendance.Attendance;
import org.operation.attendance.AttendanceStatus;
import org.operation.lecture.Attribute;

import java.time.format.DateTimeFormatter;

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
