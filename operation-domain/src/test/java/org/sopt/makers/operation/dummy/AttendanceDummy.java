package org.sopt.makers.operation.dummy;

import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.member.domain.Member;

public class AttendanceDummy extends Attendance {
    public AttendanceDummy(Long id, Member member, Lecture lecture, AttendanceStatus status) {
        super(id, member, lecture, status);
    }
}
