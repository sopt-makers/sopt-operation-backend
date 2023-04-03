package org.sopt.makers.operation.dto.member;

import org.sopt.makers.operation.entity.Part;

public record MemberSearchCondition(Part part, int generation) {
}
