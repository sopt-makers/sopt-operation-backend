package org.sopt.makers.operation.repository.lecture;

import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.lecture.Lecture;
import java.util.List;
import java.util.Optional;

public interface LectureCustomRepository {
    List<Lecture> find(int generation, Part part);
    List<Lecture> findLecturesToBeEnd();
    Optional<Lecture> find(Long lectureId);
}
