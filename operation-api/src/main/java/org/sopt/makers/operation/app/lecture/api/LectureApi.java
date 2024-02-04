package org.sopt.makers.operation.app.lecture.api;

import java.security.Principal;

import org.sopt.makers.operation.common.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;

@Tag(name = "앱 세션 관련 API")
public interface LectureApi {

	@Operation(
			security = @SecurityRequirement(name = "Authorization"),
			summary = "앱 내 진행 중인 세션 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "진행 중인 세션 조회 성공"
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
	ResponseEntity<BaseResponse<?>> getTodayLecture(
			@Parameter(hidden = true) @NonNull Principal principal);

	@Operation(
			security = @SecurityRequirement(name = "Authorization"),
			summary = "앱 내 출석 차수 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "출석 차수 조회 성공"
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
	ResponseEntity<BaseResponse<?>> getRound(@PathVariable long lectureId);
}
