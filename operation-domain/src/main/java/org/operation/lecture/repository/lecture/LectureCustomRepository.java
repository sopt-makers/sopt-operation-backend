package org.operation.lecture.repository.lecture;

import java.util.List;
import java.util.Optional;

import org.operation.common.domain.Part;
import org.operation.lecture.Lecture;

public interface LectureCustomRepository {
    List<Lecture> find(int generation, Part part);
    List<Lecture> findLecturesToBeEnd();
    Optional<Lecture> find(Long lectureId);
}
