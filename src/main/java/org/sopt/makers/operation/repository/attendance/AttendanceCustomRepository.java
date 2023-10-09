package org.sopt.makers.operation.repository.attendance;

import java.util.List;

import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.SubAttendance;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.springframework.data.domain.Pageable;

public interface AttendanceCustomRepository {
	List<Attendance> findAttendanceByMemberId(Long memberId);
	List<Attendance> findByLecture(Long lectureId, Part part, Pageable pageable);
	List<Attendance> findByMember(Member member);
	List<Attendance> findByLecture(Lecture lecture);
	List<Attendance> findCurrentAttendanceByMember(Long playGroundId);
	List<SubAttendance> findSubAttendanceByAttendanceId(Long attendanceId);
}
