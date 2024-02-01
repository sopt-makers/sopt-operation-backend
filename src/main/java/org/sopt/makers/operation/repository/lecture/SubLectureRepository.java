package org.sopt.makers.operation.repository.lecture;

import org.operation.lecture.SubLecture;
import org.operation.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubLectureRepository extends JpaRepository<SubLecture, Long> {
    Optional<SubLecture> findById(Long subLectureId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from SubLecture sl where sl.lecture = :lecture")
    void deleteAllByLecture(Lecture lecture);
}