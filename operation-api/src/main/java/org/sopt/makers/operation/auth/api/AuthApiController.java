package org.sopt.makers.operation.auth.api;

import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.EXPIRED_PLATFORM_CODE;
import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.EXPIRED_REFRESH_TOKEN;
import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.INVALID_GRANT_TYPE;
import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.INVALID_SOCIAL_TYPE;
import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.NOT_FOUNT_REGISTERED_TEAM;
import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.NOT_NULL_CODE;
import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.NOT_NULL_GRANT_TYPE;
import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.NOT_NULL_REFRESH_TOKEN;
import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.USED_PLATFORM_CODE;
import static org.sopt.makers.operation.code.success.auth.AuthSuccessCode.SUCCESS_GENERATE_TOKEN;
import static org.sopt.makers.operation.code.success.auth.AuthSuccessCode.SUCCESS_RETURN_REDIRECT_URL_WITH_PLATFORM_CODE;
import static org.sopt.makers.operation.jwt.JwtTokenType.PLATFORM_CODE;
import static org.sopt.makers.operation.jwt.JwtTokenType.REFRESH_TOKEN;

import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.auth.dto.request.AccessTokenRequest;
import org.sopt.makers.operation.auth.dto.response.RedirectUrlResponse;
import org.sopt.makers.operation.auth.dto.response.TokenResponse;
import org.sopt.makers.operation.auth.service.AuthService;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.exception.AuthException;
import org.sopt.makers.operation.jwt.JwtTokenProvider;
import org.sopt.makers.operation.user.domain.SocialType;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController implements AuthApi {

    private static final String AUTHORIZATION_CODE_GRANT_TYPE = "authorizationCode";
    private static final String REFRESH_TOKEN_GRANT_TYPE = "refreshToken";
    private static final String REDIRECT_URL_WITH_CODE_FORMAT = "%s?code=%s";

    private final ConcurrentHashMap<String, String> tempPlatformCode;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @GetMapping("/authorize")
    public ResponseEntity<BaseResponse<?>> authorize(
            @RequestParam String type,
            @RequestParam String code,
            @RequestParam String clientId,
            @RequestParam String redirectUri
    ) {
        if (!authService.checkRegisteredTeamOAuthInfo(clientId, redirectUri)) {
            throw new AuthException(NOT_FOUNT_REGISTERED_TEAM);
        }
        if (!SocialType.isContains(type)) {
            throw new AuthException(INVALID_SOCIAL_TYPE);
        }

        val userId = findUserIdBySocialTypeAndCode(type, code);
        val platformCode = generatePlatformCode(clientId, redirectUri, userId);
        val redirectUrl = String.format(REDIRECT_URL_WITH_CODE_FORMAT, redirectUri, platformCode);
        return ApiResponseUtil.success(SUCCESS_RETURN_REDIRECT_URL_WITH_PLATFORM_CODE, new RedirectUrlResponse(redirectUrl));
    }

    @Override
    @PostMapping(
            path = "/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<BaseResponse<?>> token(AccessTokenRequest accessTokenRequest) {
        if (accessTokenRequest.isNullGrantType()) {
            throw new AuthException(NOT_NULL_GRANT_TYPE);
        }

        val grantType = accessTokenRequest.grantType();
        if (!(grantType.equals(AUTHORIZATION_CODE_GRANT_TYPE) || grantType.equals(REFRESH_TOKEN_GRANT_TYPE))) {
            throw new AuthException(INVALID_GRANT_TYPE);
        }

        val tokenResponse = grantType.equals(AUTHORIZATION_CODE_GRANT_TYPE) ?
                generateTokenResponseByAuthorizationCode(accessTokenRequest) : generateTokenResponseByRefreshToken(accessTokenRequest);
        return ApiResponseUtil.success(SUCCESS_GENERATE_TOKEN, tokenResponse);
    }

    private TokenResponse generateTokenResponseByAuthorizationCode(AccessTokenRequest accessTokenRequest) {
        if (accessTokenRequest.isNullCode()) {
            throw new AuthException(NOT_NULL_CODE);
        }
        if (!tempPlatformCode.contains(accessTokenRequest.code())) {
            throw new AuthException(USED_PLATFORM_CODE);
        }
        tempPlatformCode.remove(accessTokenRequest.code());

        if (!jwtTokenProvider.validatePlatformCode(accessTokenRequest.code(), accessTokenRequest.clientId(), accessTokenRequest.redirectUri())) {
            throw new AuthException(EXPIRED_PLATFORM_CODE);
        }

        val authentication = jwtTokenProvider.getAuthentication(accessTokenRequest.code(), PLATFORM_CODE);
        return generateTokenResponse(authentication);
    }

    private TokenResponse generateTokenResponseByRefreshToken(AccessTokenRequest accessTokenRequest) {
        if (accessTokenRequest.isNullRefreshToken()) {
            throw new AuthException(NOT_NULL_REFRESH_TOKEN);
        }
        if (!jwtTokenProvider.validateTokenExpiration(accessTokenRequest.refreshToken(), REFRESH_TOKEN)) {
            throw new AuthException(EXPIRED_REFRESH_TOKEN);
        }

        val authentication = jwtTokenProvider.getAuthentication(accessTokenRequest.refreshToken(), REFRESH_TOKEN);
        return generateTokenResponse(authentication);
    }

    private TokenResponse generateTokenResponse(Authentication authentication) {
        val accessToken = jwtTokenProvider.generateAccessToken(authentication);
        val refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        return TokenResponse.of(accessToken, refreshToken);
    }

    private Long findUserIdBySocialTypeAndCode(String type, String code) {
        val socialType = SocialType.valueOf(type);
        val userSocialId = authService.getSocialUserInfo(socialType, code);
        return authService.getUserId(socialType, userSocialId);
    }

    private String generatePlatformCode(String clientId, String redirectUri, Long userId) {
        val platformCode = jwtTokenProvider.generatePlatformCode(clientId, redirectUri, userId);
        tempPlatformCode.putIfAbsent(platformCode, platformCode);
        return platformCode;
    }
}
