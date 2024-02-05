package org.sopt.makers.operation.domain.attendance.repository.attendance;

import java.util.List;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.domain.attendance.domain.Attendance;
import org.sopt.makers.operation.domain.lecture.Lecture;
import org.sopt.makers.operation.domain.member.domain.Member;
import org.springframework.data.domain.Pageable;

public interface AttendanceCustomRepository {
	List<Attendance> findAttendanceByMemberId(Long memberId);
	List<Attendance> findFetchJoin(Lecture lecture, Part part, Pageable pageable);
	List<Attendance> findFetchJoin(Member member);
	List<Attendance> findToday(long memberPlaygroundId);
	int count(Lecture lecture, Part part);
}
