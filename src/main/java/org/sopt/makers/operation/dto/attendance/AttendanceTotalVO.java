package org.sopt.makers.operation.dto.attendance;

import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.AttendanceStatus;

import java.time.format.DateTimeFormatter;

public record AttendanceTotalVO(
        String name,
        AttendanceStatus status,
        String date
) {
    public static AttendanceTotalVO of(Attendance attendance){
        return new AttendanceTotalVO(
                attendance.getLecture().getName(),
                attendance.getStatus(),
                attendance.getLecture().getStartDate()
                        .format(DateTimeFormatter.ofPattern("M월 d일"))
        );
    }
}
