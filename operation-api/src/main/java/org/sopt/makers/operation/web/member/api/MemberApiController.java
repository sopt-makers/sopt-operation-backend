package org.sopt.makers.operation.web.member.api;

import static org.sopt.makers.operation.web.member.message.SuccessMessage.*;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.common.dto.BaseResponse;
import org.sopt.makers.operation.common.util.ApiResponseUtil;
import org.sopt.makers.operation.web.member.service.MemberService;
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
public class MemberApiController implements MemberApi {

	private final MemberService memberService;

	@Override
	@GetMapping("/list")
	public ResponseEntity<BaseResponse<?>> getMemberList(
			@RequestParam(required = false) Part part,
			@RequestParam(required = false) Integer generation,
			Pageable pageable
	) {
		val response = memberService.getMemberList(part, generation, pageable);
		return ApiResponseUtil.ok(SUCCESS_GET_MEMBERS.getContent(), response);
	}
}
