package org.operation.app.attendance;

import java.security.Principal;

import org.operation.app.attendance.dto.request.AttendanceRequest;
import org.operation.common.dto.BaseResponse;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "앱 출석 관련 API")
public interface AttendanceApi {
	@Operation(
		summary = "앱 출석 API",
		responses = {
				@ApiResponse(
					responseCode = "200",
					description = "출석에 성공했습니다."
				),
				@ApiResponse(
					 responseCode = "400",
					 description = "출석에 실패했습니다."
				),
				@ApiResponse(
						responseCode = "500",
						description = "서버 내부 오류"
				)
		}
	)
	ResponseEntity<BaseResponse<?>> attend(
			@RequestBody AttendanceRequest request,
			@Parameter(hidden = true) Principal principal
	);
}
