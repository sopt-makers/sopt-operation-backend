package org.operation.app.member.api;

import static org.operation.app.member.message.SuccessMessage.*;

import java.security.Principal;

import org.operation.app.member.service.MemberService;
import org.operation.common.dto.BaseResponse;
import org.operation.common.util.ApiResponseUtil;
import org.operation.common.util.CommonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/members")
public class MemberApiController implements MemberApi {

	private final MemberService memberService;
	private final CommonUtils utils;

	@Override
	@GetMapping("/attendances")
	public ResponseEntity<BaseResponse<?>> getMemberTotalAttendance(@NonNull Principal principal) {
		val memberId = utils.getMemberId(principal);
		val response = memberService.getMemberTotalAttendance(memberId);
		return ApiResponseUtil.ok(SUCCESS_TOTAL_ATTENDANCE.getContent(), response);
	}

	@Override
	@GetMapping("/score")
	public ResponseEntity<BaseResponse<?>> getScore(@NonNull Principal principal) {
		val memberId = utils.getMemberId(principal);
		val response = memberService.getMemberScore(memberId);
		return ApiResponseUtil.ok(SUCCESS_GET_ATTENDANCE_SCORE.getContent(), response);
	}
}
