package org.sopt.makers.operation.attendance.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.dummy.AttendanceDummy;
import org.sopt.makers.operation.dummy.LectureDummy;
import org.sopt.makers.operation.dummy.MemberDummy;
import org.sopt.makers.operation.dummy.SubAttendanceDummy;
import org.sopt.makers.operation.dummy.SubLectureDummy;
import org.sopt.makers.operation.lecture.domain.Attribute;
import org.sopt.makers.operation.lecture.domain.LectureStatus;
import org.sopt.makers.operation.lecture.domain.SubLecture;
import org.sopt.makers.operation.member.domain.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AttendanceGetStatusTest {

    private Member member;
    private LectureDummy lecture;
    private SubLecture subLecture1;
    private SubLecture subLecture2;
    private List<SubLecture> subLectures;
    private List<Attendance> attendances;
    private Attendance attendance;

    @BeforeEach
    void setUp() {
        member = MemberDummy.builder()
                .id(1L)
                .attendances(new ArrayList<>())
                .build();

        lecture = new LectureDummy(1L, "서버 1차 세미나", Part.SERVER, 34, "오산역", LocalDateTime.now(), LocalDateTime.now(), Attribute.SEMINAR, LectureStatus.BEFORE);

        subLecture1 = new SubLectureDummy(1L, lecture, 1, LocalDateTime.now(), "000001");
        subLecture2 = new SubLectureDummy(2L, lecture, 2, LocalDateTime.now(), "000002");

        subLectures = new ArrayList<>();
        subLectures.add(subLecture1);
        subLectures.add(subLecture2);

        attendances = new ArrayList<>();
        attendance = new AttendanceDummy(1L, member, lecture, AttendanceStatus.ABSENT);
        attendances.add(attendance);

        lecture.setOneToMany(subLectures, attendances);
    }

    @Test
    @DisplayName("세미나 1차 출석을 했고, 2차 출석을 했으면 출석 처리가 된다.")
    void getStatusTest() {
        // given
        SubAttendance subAttendance1 = new SubAttendanceDummy(1L, attendance, subLecture1, AttendanceStatus.ATTENDANCE);
        SubAttendance subAttendance2 = new SubAttendanceDummy(2L, attendance, subLecture2, AttendanceStatus.ATTENDANCE);

        // when
        AttendanceStatus attendanceStatus = attendance.getStatus();

        // then
        assertThat(attendanceStatus).isEqualTo(AttendanceStatus.ATTENDANCE);
    }

    @Test
    @DisplayName("세미나에서 1차 출석을 했고, 2차 출석을 결석했으면 지각 처리가 된다.")
    void getStatusTest2() {
        // given
        SubAttendance subAttendance1 = new SubAttendanceDummy(1L, attendance, subLecture1, AttendanceStatus.ATTENDANCE);
        SubAttendance subAttendance2 = new SubAttendanceDummy(2L, attendance, subLecture2, AttendanceStatus.ABSENT);

        // when
        AttendanceStatus attendanceStatus = attendance.getStatus();

        // then
        assertThat(attendanceStatus).isEqualTo(AttendanceStatus.TARDY);
    }

    @Test
    @DisplayName("세미나에서 1차 출석을 결석했고, 2차 출석을 했으면 지각 처리가 된다.")
    void getStatusTest3() {
        // given
        SubAttendance subAttendance1 = new SubAttendanceDummy(1L, attendance, subLecture1, AttendanceStatus.ABSENT);
        SubAttendance subAttendance2 = new SubAttendanceDummy(2L, attendance, subLecture2, AttendanceStatus.ATTENDANCE);

        // when
        AttendanceStatus attendanceStatus = attendance.getStatus();

        // then
        assertThat(attendanceStatus).isEqualTo(AttendanceStatus.TARDY);
    }

    @Test
    @DisplayName("세미나에서 1차 출석을 결석했고, 2차 출석을 결석했으면 결석 처리가 된다.")
    void getStatusTest4() {
        // given
        SubAttendance subAttendance1 = new SubAttendanceDummy(1L, attendance, subLecture1, AttendanceStatus.ABSENT);
        SubAttendance subAttendance2 = new SubAttendanceDummy(2L, attendance, subLecture2, AttendanceStatus.ABSENT);

        // when
        AttendanceStatus attendanceStatus = attendance.getStatus();

        // then
        assertThat(attendanceStatus).isEqualTo(AttendanceStatus.ABSENT);
    }

}