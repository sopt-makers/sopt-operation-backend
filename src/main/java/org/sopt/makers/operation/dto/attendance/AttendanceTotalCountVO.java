package org.sopt.makers.operation.dto.attendance;

public record AttendanceTotalCountVO(
        int total,
        int attendance,
        int absent,
        int tardy
) {
    public static AttendanceTotalCountVO of(int total, int attendance, int absent, int tardy){
        return new AttendanceTotalCountVO(
                total,
                attendance,
                absent,
                tardy
        );
    }
}
