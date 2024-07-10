package org.sopt.makers.operation.user.api;

import lombok.val;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.code.failure.UserFailureCode;
import org.sopt.makers.operation.common.util.CommonUtils;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.exception.ParameterDecodeCustomException;
import org.sopt.makers.operation.exception.UserException;
import org.sopt.makers.operation.user.service.UserService;
import org.sopt.makers.operation.util.ApiResponseUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static org.sopt.makers.operation.code.success.UserSuccessCode.SUCCESS_GET_USER;
import static org.sopt.makers.operation.code.success.UserSuccessCode.SUCCESS_GET_USERS;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/user")
public class UserApiController implements UserApi {

	private static final String DECODING_CHARSET = "UTF-8";
	private static final String DELIMITER_ID_PARAMETER = ",";
	private static final String USER_IDS_PARAMETER_REGEX = "^(\\d+)(%2C\\d+)*$";

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
	@GetMapping
	public ResponseEntity<BaseResponse<?>> getUserInfoOf(
			@NonNull Principal principal,
			@NonNull @RequestParam("userIds") String targetUserIds
	) {
		val userIds = getIdsFromParameter(targetUserIds);
		val response = userService.getUserInfos(userIds);
		return ApiResponseUtil.success(SUCCESS_GET_USERS, response);
	}

	private List<Long> getIdsFromParameter(String parameter)  {
		if (Objects.isNull(parameter) || !parameter.matches(USER_IDS_PARAMETER_REGEX)) {
			throw new UserException(UserFailureCode.INVALID_PARAMETER);
		}
		try {
			val decodedParameter = URLDecoder.decode(parameter, DECODING_CHARSET);
			return Arrays.stream(decodedParameter.split(DELIMITER_ID_PARAMETER))
					.filter(str -> !str.equals(DELIMITER_ID_PARAMETER))
					.map(id -> Long.parseLong(id.trim()))
					.toList();
		} catch (UnsupportedEncodingException | NumberFormatException ex) {
			throw new ParameterDecodeCustomException(UserFailureCode.INVALID_PARAMETER, parameter);
		}
	}

}
