package org.sopt.makers.operation.repository.attendance;

import org.sopt.makers.operation.entity.attendance.Attendance;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>, AttendanceCustomRepository {
    Optional<Attendance> findAttendanceByLectureIdAndMemberId(Long lectureId, Long memberId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Attendance a where a.lecture = :lecture")
    void deleteAllByLecture(Lecture lecture);
}
