package org.sopt.makers.operation.repository.lecture;

import org.sopt.makers.operation.entity.SubLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubLectureRepository extends JpaRepository<SubLecture, Long> {
    Optional<SubLecture> findById(Long subLectureId);
}
