package org.sopt.makers.operation.dummy;

import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.domain.SubLecture;

import java.time.LocalDateTime;

public class SubLectureDummy extends SubLecture {

    public SubLectureDummy(Long id, Lecture lecture, int round, LocalDateTime startAt, String code) {
        super(id, lecture, round, startAt, code);
    }
}
