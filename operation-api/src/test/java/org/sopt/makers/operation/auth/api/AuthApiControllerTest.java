package org.sopt.makers.operation.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sopt.makers.operation.auth.service.AuthService;
import org.sopt.makers.operation.common.handler.ErrorHandler;
import org.sopt.makers.operation.user.domain.SocialType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = AuthApiController.class)
@WebMvcTest(controllers = {AuthApiController.class}, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import({AuthApiController.class, ErrorHandler.class})
class AuthApiControllerTest {
    @MockBean
    AuthService authService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    final String uri = "/api/v1/authorize";

    @Nested
    @DisplayName("API 통신 성공 테스트")
    class SuccessTest {
        @DisplayName("유효한 type, code, clientId, redirectUri 값이 들어왔을 때, 플랫폼 인가코드를 반환한다.")
        @ParameterizedTest
        @CsvSource({
                "APPLE,code,clientId,redirectUri"
        })
        void successTest(String type, String code, String clientId, String redirectUri) throws Exception {
            // given
            given(authService.checkRegisteredTeamOAuthInfo(clientId, redirectUri)).willReturn(true);
            val socialType = SocialType.valueOf(type);
            given(authService.getSocialUserInfo(socialType, code)).willReturn("123");
            given(authService.getUserId(socialType, "123")).willReturn(1L);
            given(authService.generatePlatformCode(clientId, redirectUri, 1L)).willReturn("Platform Code");

            // when, then
            mockMvc.perform(get(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("type", type)
                            .param("code", code)
                            .param("clientId", clientId)
                            .param("redirectUri", redirectUri))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("플랫폼 인가코드 발급 성공"));
        }
    }

    @Nested
    @DisplayName("쿼리 파라미터 유효성 검사 테스트")
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
            mockMvc.perform(get(uri)
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
            mockMvc.perform(get(uri)
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
            mockMvc.perform(get(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("type", type)
                            .param("code", code)
                            .param("clientId", clientId)
                            .param("redirectUri", redirectUri))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("유효하지 않은 social type 입니다."));
        }
    }
}