package org.sopt.makers.operation.dto.member;

import org.sopt.makers.operation.entity.Gender;
import org.sopt.makers.operation.entity.ObYb;
import org.sopt.makers.operation.entity.Part;

import com.sun.istack.NotNull;

public record MemberRequestDTO(
	@NotNull String name,
	@NotNull int generation,
	ObYb obyb,
	@NotNull Part part,
	Gender gender,
	String university,
	String phone
) {
}
