package org.operation.attendance.repository.attendance;

import java.util.List;

import org.operation.attendance.domain.Attendance;
import org.operation.common.domain.Part;
import org.operation.member.domain.Member;
import org.springframework.data.domain.Pageable;

public interface AttendanceCustomRepository {
	List<Attendance> findAttendanceByMemberId(Long memberId);
	List<Attendance> findByLecture(Long lectureId, Part part, Pageable pageable);
	List<Attendance> findByMember(Member member);
	List<Attendance> findToday(long memberPlaygroundId);
	int countByLectureIdAndPart(long lectureId, Part part);
}
