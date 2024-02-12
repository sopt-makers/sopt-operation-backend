package org.sopt.makers.operation.lecture.repository.subLecture;

import java.util.Optional;

import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.domain.SubLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubLectureRepository extends JpaRepository<SubLecture, Long> {
    Optional<SubLecture> findById(Long subLectureId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from SubLecture sl where sl.lecture = :lecture")
    void deleteAllByLecture(Lecture lecture);
}