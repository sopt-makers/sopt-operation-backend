package org.sopt.makers.operation.dto.member;

import org.sopt.makers.operation.entity.Gender;
import org.sopt.makers.operation.entity.ObYb;
import org.sopt.makers.operation.entity.Part;

public record MemberRequestDTO(
	Long playgroundId,
	String name,
	int generation,
	ObYb obyb,
	Part part,
	Gender gender,
	String university,
	String phone
) {
}
