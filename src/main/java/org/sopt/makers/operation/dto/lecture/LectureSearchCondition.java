package org.sopt.makers.operation.dto.lecture;

import org.sopt.makers.operation.entity.member.Member;
import org.sopt.makers.operation.entity.Part;

public record LectureSearchCondition(Part part, int generation, Long memberId) {
    public static LectureSearchCondition of(Member member) {
        return new LectureSearchCondition(
            member.getPart(),
            member.getGeneration(),
            member.getId()
        );
    }
}
