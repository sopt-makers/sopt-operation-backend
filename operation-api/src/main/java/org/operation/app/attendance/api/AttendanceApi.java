package org.operation.app.attendance.api;

import java.security.Principal;

import org.operation.app.attendance.dto.AttendanceRequest;
import org.operation.common.dto.BaseResponse;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;

@Tag(name = "앱 출석 관련 API")
public interface AttendanceApi {

	@Operation(
		security = @SecurityRequirement(name = "Authorization"),
		summary = "앱 출석 API",
		responses = {
				@ApiResponse(
					responseCode = "200",
					description = "출석 성공"
				),
				@ApiResponse(
					 responseCode = "400",
					 description = "잘못된 요청"
				),
				@ApiResponse(
						responseCode = "500",
						description = "서버 내부 오류"
				)
		}
	)
	ResponseEntity<BaseResponse<?>> attend(
			@RequestBody AttendanceRequest request,
			@Parameter(hidden = true) @NonNull Principal principal);
}
