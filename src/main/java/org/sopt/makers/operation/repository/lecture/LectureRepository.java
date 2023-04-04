package org.sopt.makers.operation.repository.lecture;

import java.util.List;

import org.sopt.makers.operation.entity.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
	List<Lecture> findByGenerationOrderByStartDateDesc(int generation);
}
