package org.sopt.makers.operation.dto.lecture;

import org.sopt.makers.operation.entity.AttendanceStatus;
import org.sopt.makers.operation.entity.Part;

import java.time.LocalDateTime;

public record LectureSearchCondition(Part part, int generation, Long memberId) {
    public static LectureSearchCondition of(Part part, int generation, Long memberId){
        return new LectureSearchCondition(
                part,
                generation,
                memberId
        );
    }
}
