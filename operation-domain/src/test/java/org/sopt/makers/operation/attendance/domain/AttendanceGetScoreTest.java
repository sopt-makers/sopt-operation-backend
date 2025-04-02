package org.sopt.makers.operation.attendance.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.dummy.AttendanceDummy;
import org.sopt.makers.operation.dummy.LectureDummy;
import org.sopt.makers.operation.dummy.MemberDummy;
import org.sopt.makers.operation.lecture.domain.Attribute;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.domain.LectureStatus;
import org.sopt.makers.operation.member.domain.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AttendanceGetScoreTest {

    private Member member;

    @BeforeEach
    void setUp() {
        member = MemberDummy.builder()
                .id(1L)
                .attendances(new ArrayList<>())
                .build();
    }

    @Nested
    @DisplayName("[세미나 출석 점수 반환 테스트]")
    public class SeminarTest {
        @Test
        @DisplayName("세미나에 출석을 했으면 0f를 반환한다.")
        public void getScoreTest() {
            // given
            Lecture lecture = new LectureDummy(1L, "서버 1차 세미나", Part.SERVER, 34, "오산역", LocalDateTime.now(), LocalDateTime.now(), Attribute.SEMINAR, LectureStatus.BEFORE);
            Attendance attendance = new AttendanceDummy(1L, member, lecture, AttendanceStatus.ATTENDANCE);

            // when
            float score = attendance.getScore();

            // then
            assertThat(score).isEqualTo(0f);
        }

        @Test
        @DisplayName("세미나에 지각을 했으면 -0.5f를 반환한다.")
        public void getScoreTest2() {
            // given
            Lecture lecture = new LectureDummy(1L, "서버 1차 세미나", Part.SERVER, 34, "오산역", LocalDateTime.now(), LocalDateTime.now(), Attribute.SEMINAR, LectureStatus.BEFORE);
            Attendance attendance = new AttendanceDummy(1L, member, lecture, AttendanceStatus.TARDY);

            // when
            float score = attendance.getScore();

            // then
            assertThat(score).isEqualTo(-0.5f);
        }

        @Test
        @DisplayName("세미나에 결석을 했으면 -1f를 반환한다.")
        public void getScoreTest3() {
            // given
            Lecture lecture = new LectureDummy(1L, "서버 1차 세미나", Part.SERVER, 34, "오산역", LocalDateTime.now(), LocalDateTime.now(), Attribute.SEMINAR, LectureStatus.BEFORE);
            Attendance attendance = new AttendanceDummy(1L, member, lecture, AttendanceStatus.ABSENT);

            // when
            float score = attendance.getScore();

            // then
            assertThat(score).isEqualTo(-1f);
        }
    }

    @Nested
    @DisplayName("[행사 출석 점수 반환 테스트]")
    public class EventTest {
        @Test
        @DisplayName("행사에 출석을 했으면 0.5f를 반환한다.")
        public void getScoreTest() {
            // given
            Lecture lecture = new LectureDummy(1L, "서버 1차 세미나", Part.SERVER, 34, "오산역", LocalDateTime.now(), LocalDateTime.now(), Attribute.EVENT, LectureStatus.BEFORE);
            Attendance attendance = new AttendanceDummy(1L, member, lecture, AttendanceStatus.ATTENDANCE);

            // when
            float score = attendance.getScore();

            // then
            assertThat(score).isEqualTo(0.5f);
        }

        @Test
        @DisplayName("행사에 지각을 했으면 0.5f를 반환한다.")
        public void getScoreTest2() {
            // given
            Lecture lecture = new LectureDummy(1L, "서버 1차 세미나", Part.SERVER, 34, "오산역", LocalDateTime.now(), LocalDateTime.now(), Attribute.EVENT, LectureStatus.BEFORE);
            Attendance attendance = new AttendanceDummy(1L, member, lecture, AttendanceStatus.TARDY);

            // when
            float score = attendance.getScore();

            // then
            assertThat(score).isEqualTo(0.5f);
        }

        @Test
        @DisplayName("행사에 결석을 했으면 0f를 반환한다.")
        public void getScoreTest3() {
            // given
            Lecture lecture = new LectureDummy(1L, "서버 1차 세미나", Part.SERVER, 34, "오산역", LocalDateTime.now(), LocalDateTime.now(), Attribute.EVENT, LectureStatus.BEFORE);
            Attendance attendance = new AttendanceDummy(1L, member, lecture, AttendanceStatus.ABSENT);

            // when
            float score = attendance.getScore();

            // then
            assertThat(score).isEqualTo(0f);
        }
    }
}
