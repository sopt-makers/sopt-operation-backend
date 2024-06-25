package org.sopt.makers.operation.user.api;

import lombok.val;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.common.util.CommonUtils;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.user.service.UserService;
import org.sopt.makers.operation.util.ApiResponseUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.sopt.makers.operation.code.success.UserSuccessCode.SUCCESS_GET_USER;
import static org.sopt.makers.operation.code.success.UserSuccessCode.SUCCESS_GET_USERS;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/user")
public class UserApiController implements UserApi {

	private final UserService userService;
	private final CommonUtils utils;

	@Override
	@GetMapping("/me")
	public ResponseEntity<BaseResponse<?>> getUserInfoSelf(
			@NonNull Principal principal
	) {
		val userId = utils.getMemberId(principal);
		val response = userService.getUserInfo(userId);
		return ApiResponseUtil.success(SUCCESS_GET_USER, response);
	}

	@Override
	public ResponseEntity<BaseResponse<?>> getUserInfoOf(
			@NonNull Principal principal,
			@NonNull String targetUserIds
	) {
		val userIds = utils.getIdsFromParameter(targetUserIds);
		val response = userService.getUserInfos(userIds);
		return ApiResponseUtil.success(SUCCESS_GET_USERS, response);
	}


}
