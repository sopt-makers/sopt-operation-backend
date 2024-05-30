package org.sopt.makers.operation.auth.api;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.auth.dto.response.AuthorizationCodeResponse;
import org.sopt.makers.operation.auth.service.AuthService;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.exception.AuthException;
import org.sopt.makers.operation.user.domain.SocialType;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.INVALID_SOCIAL_TYPE;
import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.NOT_FOUNT_REGISTERED_TEAM;
import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.NOT_NULL_PARAMS;
import static org.sopt.makers.operation.code.success.auth.AuthSuccessCode.SUCCESS_GET_AUTHORIZATION_CODE;

@RestController
@RequiredArgsConstructor
public class AuthApiController implements AuthApi {
    private final ConcurrentHashMap<String, String> tempPlatformCode = new ConcurrentHashMap<>();
    private final AuthService authService;

    @Override
    @GetMapping("/api/v1/authorize")
    public ResponseEntity<BaseResponse<?>> authorize(
            @RequestParam String type,
            @RequestParam String code,
            @RequestParam String clientId,
            @RequestParam String redirectUri
    ) {
        if (checkParamsIsNull(type, code, clientId, redirectUri)) throw new AuthException(NOT_NULL_PARAMS);
        if (authService.checkRegisteredTeamOAuthInfo(clientId, redirectUri))
            throw new AuthException(NOT_FOUNT_REGISTERED_TEAM);
        if (!SocialType.isContains(type)) throw new AuthException(INVALID_SOCIAL_TYPE);

        val userId = findUserIdBySocialTypeAndCode(type, code);
        val platformCode = generatePlatformCode(clientId, redirectUri, userId);
        return ApiResponseUtil.success(SUCCESS_GET_AUTHORIZATION_CODE, new AuthorizationCodeResponse(platformCode));
    }

    private boolean checkParamsIsNull(String... params) {
        for (String param : params) {
            if (param == null) return true;
        }
        return false;
    }

    private Long findUserIdBySocialTypeAndCode(String type, String code) {
        val socialType = SocialType.valueOf(type);
        val userSocialId = authService.getSocialUserInfo(socialType, code);
        return authService.getUserId(socialType, userSocialId);
    }

    private String generatePlatformCode(String clientId, String redirectUri, Long userId) {
        val platformCode = authService.generatePlatformCode(clientId, redirectUri, userId);
        tempPlatformCode.putIfAbsent(platformCode, platformCode);
        return platformCode;
    }
}
