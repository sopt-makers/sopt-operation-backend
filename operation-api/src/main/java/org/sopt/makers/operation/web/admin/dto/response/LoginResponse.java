package org.sopt.makers.operation.web.admin.dto.response;

import org.sopt.makers.operation.admin.domain.Admin;
import org.sopt.makers.operation.admin.domain.AdminStatus;
import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record LoginResponse(
		LoginResponseVO loginResponseVO,
		String refreshToken
) {

	public static LoginResponse of(Admin admin, String accessToken) {
		return builder()
				.loginResponseVO(LoginResponseVO.of(admin, accessToken))
				.refreshToken(admin.getRefreshToken())
				.build();
	}

	@Builder
	record LoginResponseVO(
			Long id,
			String name,
			AdminStatus adminStatus,
			String accessToken
	) {

		public static LoginResponseVO of(Admin admin, String accessToken) {
			return builder()
					.id(admin.getId())
					.name(admin.getName())
					.adminStatus(admin.getStatus())
					.accessToken(accessToken)
					.build();
		}
	}
}
