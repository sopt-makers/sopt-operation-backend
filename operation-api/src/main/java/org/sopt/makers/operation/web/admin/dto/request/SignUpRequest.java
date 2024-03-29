package org.sopt.makers.operation.web.admin.dto.request;

import org.sopt.makers.operation.admin.domain.Admin;
import org.sopt.makers.operation.admin.domain.Role;

public record SignUpRequest(
        String email,
        String password,
        String name,
        Role role
) {

	public Admin toEntity(String encodedPassword) {
		return Admin.builder()
				.email(this.email)
				.password(encodedPassword)
				.name(this.name)
				.role(this.role)
				.build();
	}
}
