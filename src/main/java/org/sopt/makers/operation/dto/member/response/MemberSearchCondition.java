package org.sopt.makers.operation.dto.member.response;

import org.sopt.makers.operation.entity.Part;

public record MemberSearchCondition(Part part, int generation) {
}
