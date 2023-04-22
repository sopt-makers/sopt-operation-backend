package org.sopt.makers.operation.repository.attendance;

import org.sopt.makers.operation.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>, AttendanceCustomRepository {
    Optional<Attendance> findAttendanceByLectureIdAndMemberId(Long lectureId, Long memberId);
}
