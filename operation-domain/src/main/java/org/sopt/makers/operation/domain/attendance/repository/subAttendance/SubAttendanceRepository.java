package org.sopt.makers.operation.domain.attendance.repository.subAttendance;

import java.util.List;

import org.sopt.makers.operation.domain.lecture.SubLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubAttendanceRepository extends JpaRepository<org.sopt.makers.operation.domain.attendance.domain.SubAttendance, Long> {
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("delete from SubAttendance sa where sa.subLecture in :subLectures")
	void deleteAllBySubLectureIn(List<SubLecture> subLectures);
}
