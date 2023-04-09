package org.sopt.makers.operation.repository.attendance;

import org.sopt.makers.operation.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AttendanceRepository extends JpaRepository<Attendance, Long>, AttendanceCustomRepository {
    Attendance findAttendanceByLectureIdAndMemberId(Long lectureId, Long memberId);
}
