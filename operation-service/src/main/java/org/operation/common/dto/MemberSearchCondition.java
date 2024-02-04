package org.operation.common.dto;

import org.operation.common.domain.Part;

public record MemberSearchCondition(
		Part part,
		int generation
) {
}
