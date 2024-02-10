package org.sopt.makers.operation.web.admin.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.sopt.makers.operation.domain.admin.domain.Admin;
import org.sopt.makers.operation.domain.admin.domain.Role;

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
