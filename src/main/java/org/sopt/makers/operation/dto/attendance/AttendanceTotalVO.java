package org.sopt.makers.operation.dto.attendance;

import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.lecture.Attribute;

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
