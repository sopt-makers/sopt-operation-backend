package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ResponseMessage.*;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.member.MemberRequestDTO;
import org.sopt.makers.operation.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/v1")
public class InternalOpenApiController {

	private final MemberService memberService;

	@Value("${spring.secretKey.playground}")
	private String playgroundKey;

	@ApiOperation(value = "회원 등록")
	@PostMapping("/idp/members")
	public ResponseEntity<ApiResponse> createMember(
		@RequestHeader("x-api-key") String apiKey,
		@RequestHeader("x-request-from") String serviceName,
		@RequestBody MemberRequestDTO requestDTO
	) throws IllegalAccessException {
		checkApiKey(apiKey, serviceName);
		memberService.createMember(requestDTO);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_CREATE_MEMBER.getMessage()));
	}

	private void checkApiKey(String apiKey, String serviceName) throws IllegalAccessException {
		if (serviceName.equals("playground") && apiKey.equals(playgroundKey)) {
			return;
		} else {
			throw new IllegalAccessException("잘못된 API Key 입니다.");
		}
	}

}
