package org.sopt.makers.operation.web.admin.dto.response;

import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record TokenRefreshGetResponse(
		String accessToken
) {

	public static TokenRefreshGetResponse of(String accessToken) {
		return TokenRefreshGetResponse.builder()
				.accessToken(accessToken)
				.build();
	}
}