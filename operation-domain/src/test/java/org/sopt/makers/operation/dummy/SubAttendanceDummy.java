package org.sopt.makers.operation.dummy;

import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.attendance.domain.SubAttendance;
import org.sopt.makers.operation.lecture.domain.SubLecture;

public class SubAttendanceDummy extends SubAttendance {

    public SubAttendanceDummy(Long id, Attendance attendance, SubLecture subLecture, AttendanceStatus status) {
        super(id, attendance, subLecture, status);
    }
}
