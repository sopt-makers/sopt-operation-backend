package org.sopt.makers.operation.web.admin.dto.request;

import org.operation.admin.domain.Admin;
import org.operation.admin.domain.Role;

public record SignUpRequest(
        String email,
        String password,
        String name,
        Role role
) {

	public Admin toEntity() {
		return Admin.builder()
				.email(this.email)
				.password(this.password)
				.name(this.name)
				.role(this.role)
				.build();
	}
}
