package org.sopt.makers.operation.dummy;

import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.lecture.domain.Attribute;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.domain.LectureStatus;
import org.sopt.makers.operation.lecture.domain.SubLecture;

import java.time.LocalDateTime;
import java.util.List;

public class LectureDummy extends Lecture {

    public LectureDummy(Long id, String name, Part part, int generation, String place, LocalDateTime startDate, LocalDateTime endDate, Attribute attribute, LectureStatus lectureStatus) {
        super(id, name, part, generation, place, startDate, endDate, attribute, lectureStatus);
    }

    public void setOneToMany(List<SubLecture> subLectures, List<Attendance> attendances) {
        super.setOneToMany(subLectures, attendances);
    }
}
