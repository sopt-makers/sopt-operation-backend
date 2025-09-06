package org.sopt.makers.operation.attendance.repository.attendance;

import java.util.List;

import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.member.domain.Member;
import org.springframework.data.domain.Pageable;

public interface AttendanceCustomRepository {
	List<Attendance> findAttendanceByMemberId(Long memberId);
	List<Attendance> findFetchJoin(Lecture lecture, Part part, Pageable pageable);
	List<Attendance> findFetchJoin(Member member);
	List<Attendance> findToday(long memberId);
	int count(Lecture lecture, Part part);
}
