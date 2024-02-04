package org.sopt.makers.operation.service.web.admin.dto.response;

import org.operation.admin.domain.Admin;
import org.operation.admin.domain.Role;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record SignUpResponse(
        Long id,
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
