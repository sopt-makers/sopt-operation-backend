package org.sopt.makers.operation.domain.lecture.repository.lecture;

import org.sopt.makers.operation.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureCustomRepository {
}
