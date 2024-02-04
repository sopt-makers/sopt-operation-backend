package org.operation.lecture.repository.lecture;

import org.operation.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureCustomRepository  {
}
