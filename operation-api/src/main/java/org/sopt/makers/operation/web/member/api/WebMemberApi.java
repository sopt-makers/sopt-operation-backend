package org.sopt.makers.operation.web.member.api;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.dto.BaseResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface WebMemberApi {

	@Operation(
			summary = "멤버 리스트 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "멤버 리스트 조회 성공"
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
	ResponseEntity<BaseResponse<?>> getMembers(
			@RequestParam(required = false) Part part,
			@RequestParam int generation,
			Pageable pageable);
}
