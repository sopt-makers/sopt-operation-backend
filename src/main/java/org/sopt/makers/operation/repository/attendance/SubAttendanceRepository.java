package org.sopt.makers.operation.repository.attendance;

import java.util.List;

import org.operation.attendance.SubAttendance;
import org.operation.lecture.SubLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubAttendanceRepository extends JpaRepository<SubAttendance, Long> {
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("delete from SubAttendance sa where sa.subLecture in :subLectures")
	void deleteAllBySubLectureIn(List<SubLecture> subLectures);
}
