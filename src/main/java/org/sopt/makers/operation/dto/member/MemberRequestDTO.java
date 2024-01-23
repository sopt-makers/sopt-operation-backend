package org.sopt.makers.operation.dto.member;

import org.sopt.makers.operation.entity.member.ObYb;
import org.sopt.makers.operation.entity.Part;

public record MemberRequestDTO(
	Long playgroundId,
	String name,
	int generation,
	ObYb obyb,
	Part part,
	String university,
	String phone
) {
}
