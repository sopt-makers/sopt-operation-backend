package org.sopt.makers.operation.repository.attendance;

import java.util.List;
import java.util.Optional;

import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.springframework.data.domain.Pageable;

public interface AttendanceCustomRepository {
	Long countAttendance(Lecture lecture);
	Long countAbsent(Lecture lecture);
	Long countTardy(Lecture lecture);
	List<Attendance> findAttendanceByMemberId(Long memberId);
	List<Attendance> findAttendancesByLecture(Long lectureId, Part part, Pageable pageable);
	List<Attendance> findAttendancesByMember(Long memberId);
	Optional<Attendance> findAttendanceBySubAttendance(SubAttendance subAttendance);
}
