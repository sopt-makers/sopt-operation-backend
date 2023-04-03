package org.sopt.makers.operation.repository;

import org.sopt.makers.operation.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
