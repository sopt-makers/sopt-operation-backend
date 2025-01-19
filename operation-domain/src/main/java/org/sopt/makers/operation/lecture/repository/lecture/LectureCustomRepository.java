package org.sopt.makers.operation.lecture.repository.lecture;

import java.util.List;

import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.lecture.domain.Lecture;

public interface LectureCustomRepository {
    List<Lecture> find(int generation, Part part);
    List<Lecture> findLecturesReadyToEnd();
}
