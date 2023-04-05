package org.sopt.makers.operation.repository.attendance;

import org.sopt.makers.operation.entity.lecture.Lecture;

public interface AttendanceCustomRepository {
	Long countAttendance(Lecture lecture);
	Long countAbsent(Lecture lecture);
	Long countTardy(Lecture lecture);
}
