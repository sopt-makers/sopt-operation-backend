package org.sopt.makers.operation.repository.lecture;

import org.sopt.makers.operation.dto.lecture.LectureSearchCondition;
import org.sopt.makers.operation.entity.lecture.Lecture;
import java.util.List;

public interface LectureCustomRepository {
    List<Lecture> searchLecture(LectureSearchCondition lectureSearchCondition);
}
