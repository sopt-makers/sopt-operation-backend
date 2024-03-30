package org.sopt.makers.operation.app.member.api;

import static org.sopt.makers.operation.code.success.app.MemberSuccessCode.*;

import java.security.Principal;

import org.sopt.makers.operation.app.member.service.AppMemberService;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.common.util.CommonUtils;
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
public class AppMemberApiController implements AppMemberApi {

	private final AppMemberService memberService;
	private final CommonUtils utils;

	@Override
	@GetMapping("/attendances")
	public ResponseEntity<BaseResponse<?>> getMemberTotalAttendance(@NonNull Principal principal) {
		val memberId = utils.getMemberId(principal);
		val response = memberService.getMemberTotalAttendance(memberId);
		return ApiResponseUtil.success(SUCCESS_GET_TOTAL_ATTENDANCE, response);
	}

	@Override
	@GetMapping("/score")
	public ResponseEntity<BaseResponse<?>> getScore(@NonNull Principal principal) {
		val memberId = utils.getMemberId(principal);
		val response = memberService.getMemberScore(memberId);
		return ApiResponseUtil.success(SUCCESS_GET_ATTENDANCE_SCORE, response);
	}
}
