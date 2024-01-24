package org.sopt.makers.operation.repository.attendance;

import java.util.List;

import org.sopt.makers.operation.entity.attendance.Attendance;
import org.sopt.makers.operation.entity.member.Member;
import org.sopt.makers.operation.entity.Part;
import org.springframework.data.domain.Pageable;

public interface AttendanceCustomRepository {
	List<Attendance> findAttendanceByMemberId(Long memberId);
	List<Attendance> findByLecture(Long lectureId, Part part, Pageable pageable);
	List<Attendance> findByMember(Member member);
	List<Attendance> findToday(long memberPlaygroundId);
	int countByLectureIdAndPart(long lectureId, Part part);
}
