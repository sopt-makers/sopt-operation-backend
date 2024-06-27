package org.sopt.makers.operation.user.api;

import lombok.val;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.code.failure.UserFailureCode;
import org.sopt.makers.operation.common.util.CommonUtils;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.exception.ParameterDecodeCustomException;
import org.sopt.makers.operation.user.service.UserService;
import org.sopt.makers.operation.util.ApiResponseUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

import static org.sopt.makers.operation.code.success.UserSuccessCode.SUCCESS_GET_USER;
import static org.sopt.makers.operation.code.success.UserSuccessCode.SUCCESS_GET_USERS;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/user")
public class UserApiController implements UserApi {

	private static final String DECODING_CHARSET = "UTF-8";
	private static final String DELIMITER_ID_PARAMETER = ",";

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
		val userIds = getIdsFromParameter(targetUserIds);
		val response = userService.getUserInfos(userIds);
		return ApiResponseUtil.success(SUCCESS_GET_USERS, response);
	}

	private List<Long> getIdsFromParameter(@NonNull String parameter)  {
		try {
			String encodedParameter = URLEncoder.encode(parameter, DECODING_CHARSET);
			return Arrays.stream(encodedParameter.split(DELIMITER_ID_PARAMETER))
					.map(id -> Long.parseLong(id.trim()))
					.toList();
		} catch (UnsupportedEncodingException ex) {
			throw new ParameterDecodeCustomException(UserFailureCode.INVALID_PARAMETER, parameter);
		}
	}

}
