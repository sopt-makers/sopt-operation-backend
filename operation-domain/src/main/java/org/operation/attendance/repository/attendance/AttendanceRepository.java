package org.operation.attendance.repository.attendance;

import java.util.Optional;

import org.operation.attendance.domain.Attendance;
import org.operation.lecture.Lecture;
import org.operation.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>, AttendanceCustomRepository {
    Optional<Attendance> findByLectureAndMember(Lecture lecture, Member member);
    @Query("delete from Attendance a where a.lecture = :lecture")
    void deleteAllByLecture(Lecture lecture);
}
