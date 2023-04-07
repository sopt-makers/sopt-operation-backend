package org.sopt.makers.operation.repository.attendance;

import java.util.List;

import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Lecture;

public interface AttendanceCustomRepository {
	Long countAttendance(Lecture lecture);
	Long countAbsent(Lecture lecture);
	Long countTardy(Lecture lecture);
	List<Attendance> getAttendanceByPart(Lecture lecture, Part part);
	List<Attendance> findAttendanceByMemberId(Long memberId);
}
