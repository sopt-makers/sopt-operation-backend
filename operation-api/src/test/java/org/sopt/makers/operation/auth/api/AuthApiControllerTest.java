package org.sopt.makers.operation.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sopt.makers.operation.auth.service.AuthService;
import org.sopt.makers.operation.authentication.AdminAuthentication;
import org.sopt.makers.operation.common.handler.ErrorHandler;
import org.sopt.makers.operation.jwt.JwtTokenProvider;
import org.sopt.makers.operation.user.domain.SocialType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.BDDMockito.given;
import static org.sopt.makers.operation.jwt.JwtTokenType.PLATFORM_CODE;
import static org.sopt.makers.operation.jwt.JwtTokenType.REFRESH_TOKEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = AuthApiController.class)
@WebMvcTest(controllers = {AuthApiController.class}, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import({AuthApiController.class, ErrorHandler.class})
class AuthApiControllerTest {
    @MockBean
    AuthService authService;
    @MockBean
    JwtTokenProvider jwtTokenProvider;
    @MockBean
    ConcurrentHashMap<String, String> tempPlatformCode;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    final String authorizeUri = "/api/v1/auth/authorize";
    final String tokenUri = "/api/v1/auth/token";

    @Nested
    @DisplayName("API 통신 성공 테스트")
    class SuccessTest {
        @DisplayName("유효한 type, code, clientId, redirectUri 값이 들어왔을 때, 플랫폼 인가코드를 반환한다.")
        @ParameterizedTest
        @CsvSource({
                "APPLE,code,clientId,https://localhost:8080/auth/redirectUri"
        })
        void successTest(String type, String code, String clientId, String redirectUri) throws Exception {
            // given
            given(authService.checkRegisteredTeamOAuthInfo(clientId, redirectUri)).willReturn(true);
            val socialType = SocialType.valueOf(type);
            given(authService.getSocialUserInfo(socialType, code)).willReturn("123");
            given(authService.getUserId(socialType, "123")).willReturn(1L);
            given(jwtTokenProvider.generatePlatformCode(clientId, redirectUri, 1L)).willReturn("Platform Code");

            // when, then
            mockMvc.perform(get(authorizeUri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("type", type)
                            .param("code", code)
                            .param("clientId", clientId)
                            .param("redirectUri", redirectUri))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("플랫폼 인가코드를 포함한 redirect url 반환 성공"))
                    .andExpect(jsonPath("$.data.redirectUrl").value("https://localhost:8080/auth/redirectUri?code=Platform Code"));
        }

        @DisplayName("grantType 이 authorizationCode 이고, 유효한 clientId, redirectUri, code 값이 들어왔을 때, 액세스 토큰과 리프레시 토큰을 발급한다.")
        @ParameterizedTest
        @CsvSource({
                "authorizationCode,clientId,redirectUri,code"
        })
        void tokenByAuthorizationCodeSuccessTest(String grantType, String clientId, String redirectUri, String code) throws Exception {
            // given
            val authentication = new AdminAuthentication(1L, null, null);
            given(jwtTokenProvider.validatePlatformCode(code, clientId, redirectUri)).willReturn(true);
            given(jwtTokenProvider.getAuthentication(code, PLATFORM_CODE)).willReturn(authentication);
            given(jwtTokenProvider.generateAccessToken(authentication)).willReturn("access token");
            given(jwtTokenProvider.generateRefreshToken(authentication)).willReturn("refresh token");
            given(tempPlatformCode.contains(code)).willReturn(true);

            // when, then
            mockMvc.perform(post(tokenUri)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("grantType", grantType)
                            .param("clientId", clientId)
                            .param("redirectUri", redirectUri)
                            .param("code", code))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("토큰 발급 성공"))
                    .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                    .andExpect(jsonPath("$.data.accessToken").value("access token"))
                    .andExpect(jsonPath("$.data.refreshToken").value("refresh token"));
        }

        @DisplayName("grantType 이 refreshToken 이고, 유효한 refreshToken 값이 들어왔을 때, 액세스 토큰과 리프레시 토큰을 발급한다.")
        @ParameterizedTest
        @CsvSource({
                "refreshToken,refreshToken"
        })
        void tokenByRefreshTokenSuccessTest(String grantType, String refreshToken) throws Exception {
            // given
            val authentication = new AdminAuthentication(1L, null, null);
            given(jwtTokenProvider.validateTokenExpiration(refreshToken, REFRESH_TOKEN)).willReturn(true);
            given(jwtTokenProvider.getAuthentication(refreshToken, REFRESH_TOKEN)).willReturn(authentication);
            given(jwtTokenProvider.generateAccessToken(authentication)).willReturn("access token");
            given(jwtTokenProvider.generateRefreshToken(authentication)).willReturn("refresh token");

            // when, then
            mockMvc.perform(post(tokenUri)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("grantType", grantType)
                            .param("refreshToken", refreshToken)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("토큰 발급 성공"))
                    .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                    .andExpect(jsonPath("$.data.accessToken").value("access token"))
                    .andExpect(jsonPath("$.data.refreshToken").value("refresh token"));
        }
    }

    @Nested
    @DisplayName("/authorize API 쿼리 파라미터 유효성 검사 테스트")
    class QueryParameterValidateTest {

        @DisplayName("type, code, clientId, redirectUri 중 하나라도 null 이 들어오면 400을 반환한다.")
        @ParameterizedTest
        @CsvSource({
                ",code,clientId,redirectUri",
                "type,,clientId,redirectUri",
                "type,code,,redirectUri",
                "type,code,clientId,"
        })
        void validateTest(String type, String code, String clientId, String redirectUri) throws Exception {
            // when, then
            mockMvc.perform(get(authorizeUri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("type", type)
                            .param("code", code)
                            .param("clientId", clientId)
                            .param("redirectUri", redirectUri))
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("등록되지 않은 clientId, redirectUri 라면 404를 반환한다.")
        @ParameterizedTest
        @CsvSource({
                "type,code,clientId,redirectUri"
        })
        void validateTest2(String type, String code, String clientId, String redirectUri) throws Exception {
            // given
            given(authService.checkRegisteredTeamOAuthInfo(clientId, redirectUri)).willReturn(false);

            // when, then
            mockMvc.perform(get(authorizeUri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("type", type)
                            .param("code", code)
                            .param("clientId", clientId)
                            .param("redirectUri", redirectUri))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("등록되지 않은 팀입니다."));
            ;
        }

        @DisplayName("등록되지 않은 social type 이라면 400을 반환한다.")
        @ParameterizedTest
        @CsvSource({
                "KAKAO,code,clientId,redirectUri"
        })
        void validateTest3(String type, String code, String clientId, String redirectUri) throws Exception {
            // given
            given(authService.checkRegisteredTeamOAuthInfo(clientId, redirectUri)).willReturn(true);

            // when, then
            mockMvc.perform(get(authorizeUri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("type", type)
                            .param("code", code)
                            .param("clientId", clientId)
                            .param("redirectUri", redirectUri))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("유효하지 않은 social type 입니다."));
        }
    }

    @Nested
    @DisplayName("/token API 오류 케이스 테스트")
    class TokenAPIErrorCaseTest {

        @DisplayName("grantType 이 null 일 때 400 에러를 반환한다.")
        @ParameterizedTest
        @CsvSource({
                ",clientId,redirectUri,code"
        })
        void errorTest(String grantType, String clientId, String redirectUri, String code) throws Exception {
            // when, then
            mockMvc.perform(post(tokenUri)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("grantType", grantType)
                            .param("clientId", clientId)
                            .param("redirectUri", redirectUri)
                            .param("code", code))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("grantType 데이터가 들어오지 않았습니다."));
        }

        @DisplayName("grantType 이 authorizationCode 또는 refreshToken 외의 값이 들어온다면 400 에러를 반환한다.")
        @ParameterizedTest
        @CsvSource({
                "error,clientId,redirectUri,code"
        })
        void errorTest2(String grantType, String clientId, String redirectUri, String code) throws Exception {
            // when, then
            mockMvc.perform(post(tokenUri)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("grantType", grantType)
                            .param("clientId", clientId)
                            .param("redirectUri", redirectUri)
                            .param("code", code))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("유효하지 않은 grantType 입니다."));
        }

        @DisplayName("grantType 이 authorizationCode 일 때, code 값으로 null 값이 들어온다면 400 에러를 반환한다.")
        @ParameterizedTest
        @CsvSource({
                "authorizationCode,clientId,redirectUri,"
        })
        void errorTest3(String grantType, String clientId, String redirectUri, String code) throws Exception {
            // when, then
            mockMvc.perform(post(tokenUri)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("grantType", grantType)
                            .param("clientId", clientId)
                            .param("redirectUri", redirectUri)
                            .param("code", code))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("플랫폼 인가코드가 들어오지 않았습니다."));
        }

        @DisplayName("grantType 이 authorizationCode 일 때, code 값이 hashmap인 tempPlatformCode 내에 없다면 400 에러를 반환한다.")
        @ParameterizedTest
        @CsvSource({
                "authorizationCode,clientId,redirectUri,code"
        })
        void errorTest4(String grantType, String clientId, String redirectUri, String code) throws Exception {
            // given
            given(tempPlatformCode.contains(code)).willReturn(false);

            // when, then
            mockMvc.perform(post(tokenUri)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("grantType", grantType)
                            .param("clientId", clientId)
                            .param("redirectUri", redirectUri)
                            .param("code", code))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("이미 사용한 플랫폼 인가코드입니다."));
        }

        @DisplayName("grantType 이 refreshToken 일 때, refreshToken 값으로 null 값이 들어온다면 400 에러를 반환한다.")
        @ParameterizedTest
        @CsvSource({
                "refreshToken,"
        })
        void errorTest5(String grantType, String refreshToken) throws Exception {
            // when, then
            mockMvc.perform(post(tokenUri)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("grantType", grantType)
                            .param("refreshToken", refreshToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("리프레쉬 토큰이 들어오지 않았습니다."));
        }

        @DisplayName("grantType 이 authorizationCode 일 때, code 값이 만료되었다면 401 에러를 반환한다.")
        @ParameterizedTest
        @CsvSource({
                "authorizationCode,clientId,redirectUri,code"
        })
        void errorTest6(String grantType, String clientId, String redirectUri, String code) throws Exception {
            given(jwtTokenProvider.validatePlatformCode(code, clientId, redirectUri)).willReturn(false);
            given(tempPlatformCode.contains(code)).willReturn(true);
            // when, then
            mockMvc.perform(post(tokenUri)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("grantType", grantType)
                            .param("clientId", clientId)
                            .param("redirectUri", redirectUri)
                            .param("code", code))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("만료된 플랫폼 인가코드입니다."));
        }

        @DisplayName("grantType 이 refreshToken 일 때, refreshToken 값이 만료되었다면 401 에러를 반환한다.")
        @ParameterizedTest
        @CsvSource({
                "refreshToken,refreshToken"
        })
        void errorTest7(String grantType, String refreshToken) throws Exception {
            given(jwtTokenProvider.validateTokenExpiration(refreshToken, REFRESH_TOKEN)).willReturn(false);
            // when, then
            mockMvc.perform(post(tokenUri)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("grantType", grantType)
                            .param("refreshToken", refreshToken))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("만료된 리프레쉬 토큰입니다."));
        }
    }
}