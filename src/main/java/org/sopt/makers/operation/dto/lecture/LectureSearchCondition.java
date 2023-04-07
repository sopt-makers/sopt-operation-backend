package org.sopt.makers.operation.dto.lecture;

import org.sopt.makers.operation.entity.Part;
public record LectureSearchCondition(Part part, int generation) {
}
