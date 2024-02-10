package org.sopt.makers.operation.lecture.repository.lecture;

import java.util.List;
import java.util.Optional;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.lecture.domain.Lecture;

public interface LectureCustomRepository {
    List<Lecture> find(int generation, Part part);
    List<Lecture> findLecturesToBeEnd();
    Optional<Lecture> find(Long lectureId);
}
