package org.sopt.makers.operation.web.member.api;

import static org.sopt.makers.operation.code.success.web.MemberSuccessCode.*;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.web.member.service.WebMemberService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class WebMemberApiController implements WebMemberApi {

	private final WebMemberService memberService;

	@Override
	@GetMapping("/list")
	public ResponseEntity<BaseResponse<?>> getMembers(
			@RequestParam(required = false) Part part,
			@RequestParam int generation,
			Pageable pageable
	) {
		val response = memberService.getMembers(part, generation, pageable);
		return ApiResponseUtil.success(SUCCESS_GET_MEMBERS, response);
	}
}
