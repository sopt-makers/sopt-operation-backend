package org.sopt.makers.operation.service.web.admin.dto.request;

public record LoginRequest(
		String email,
		String password
) {
}
