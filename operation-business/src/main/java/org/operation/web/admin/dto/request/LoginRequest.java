package org.operation.web.admin.dto.request;

public record LoginRequest(
		String email,
		String password
) {
}
