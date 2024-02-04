package org.operation.web.member.api;

import static org.operation.web.member.message.SuccessMessage.*;

import org.operation.common.domain.Part;
import org.operation.common.dto.BaseResponse;
import org.operation.common.util.ApiResponseUtil;
import org.operation.web.member.service.MemberService;
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
