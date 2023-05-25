package org.sopt.makers.operation.repository.attendance;

import java.util.List;

import org.sopt.makers.operation.dto.attendance.AttendanceInfo;
import org.sopt.makers.operation.dto.attendance.MemberInfo;
import org.sopt.makers.operation.entity.Attendance;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Lecture;
import org.springframework.data.domain.Pageable;

public interface AttendanceCustomRepository {
	Long countAttendance(Lecture lecture);
	Long countAbsent(Lecture lecture);
	Long countTardy(Lecture lecture);
	List<Attendance> findAttendanceByMemberId(Long memberId);
	List<MemberInfo> findByMember(Member member);
	List<AttendanceInfo> findAttendancesOfMember(Member member);
	List<Attendance> findAttendancesByLecture(Long lectureId, Part part, Pageable pageable);
}
