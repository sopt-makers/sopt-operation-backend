package org.sopt.makers.operation.lecture.repository.lecture;

import org.sopt.makers.operation.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureCustomRepository {
}
