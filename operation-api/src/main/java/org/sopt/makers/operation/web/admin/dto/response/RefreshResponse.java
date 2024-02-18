package org.sopt.makers.operation.web.admin.dto.response;

import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record RefreshResponse(
		String accessToken
) {

	public static RefreshResponse of(String accessToken) {
		return RefreshResponse.builder()
				.accessToken(accessToken)
				.build();
	}
}