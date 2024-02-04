package org.sopt.makers.operation.dto;

import org.sopt.makers.operation.domain.Part;

public record MemberSearchCondition(
		Part part,
		int generation
) {
}
