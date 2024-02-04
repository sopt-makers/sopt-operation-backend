package org.sopt.makers.operation.domain.lecture.repository.subLecture;

import java.util.Optional;

import org.sopt.makers.operation.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubLectureRepository extends JpaRepository<org.sopt.makers.operation.domain.lecture.SubLecture, Long> {
    Optional<org.sopt.makers.operation.domain.lecture.SubLecture> findById(Long subLectureId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from SubLecture sl where sl.lecture = :lecture")
    void deleteAllByLecture(Lecture lecture);
}