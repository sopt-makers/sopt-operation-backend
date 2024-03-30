package org.sopt.makers.operation.web.admin.dto.response;

import org.sopt.makers.operation.admin.domain.Admin;
import org.sopt.makers.operation.admin.domain.Role;
import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record SignUpResponse(
		long id,
		String email,
		String name,
		Role role
) {

	public static SignUpResponse of(Admin admin) {
		return SignUpResponse.builder()
				.id(admin.getId())
				.email(admin.getEmail())
				.name(admin.getName())
				.role(admin.getRole())
				.build();
	}
}
