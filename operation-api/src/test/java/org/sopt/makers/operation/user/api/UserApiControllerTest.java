package org.sopt.makers.operation.user.api;

import java.security.Principal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import org.sopt.makers.operation.user.domain.*;
import org.sopt.makers.operation.user.dto.response.UserInfoResponse;
import org.sopt.makers.operation.user.service.UserService;
import org.sopt.makers.operation.jwt.JwtTokenProvider;
import org.sopt.makers.operation.common.util.CommonUtils;
import org.sopt.makers.operation.common.handler.ErrorHandler;

import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Import({UserApiController.class, ErrorHandler.class})
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {UserApiController.class})
@WebMvcTest(
        controllers = {UserApiController.class},
        excludeAutoConfiguration =  {SecurityAutoConfiguration.class}
)
@DisplayName("[[ Unit Test ]] - User Controller ")
class UserApiControllerTest {

    final String userInfoSelfUri = "/api/v1/internal/user/me";

    @MockBean
    UserService userService;

    @MockBean
    JwtTokenProvider tokenProvider;
    @MockBean
    CommonUtils commonUtils;

    @Autowired
    MockMvc mockMvc;

    @Nested
    @DisplayName("[TEST] 성공한 경우에 대한 테스트")
    class SuccessTest {

        @Test
        @DisplayName("Case1. 요청자 자신에 대한 유저 정보 조회 성공 - /internal/user/me")
        void successUserInfoSelf() throws Exception {
            User mockedUser = mock(User.class);
            UserGenerationHistory mockedHistory = mock(UserGenerationHistory.class);
            given(mockedUser.getId()).willReturn(1L);
            given(mockedUser.getName()).willReturn("TestUser");
            given(mockedUser.getPhone()).willReturn("01012345678");
            given(mockedUser.getProfileImage()).willReturn("TestImageUrl");

            given(mockedHistory.getId()).willReturn(1L);
            given(mockedHistory.getPart()).willReturn(Part.SERVER);
            given(mockedHistory.getGeneration()).willReturn(32);
            given(mockedHistory.getTeam()).willReturn(Team.OPERATION);
            given(mockedHistory.getPosition()).willReturn(Position.MEMBER);

            Principal mockPrincipal = mock(Principal.class);
            given(mockPrincipal.getName()).willReturn("1");

            UserInfoResponse expectServiceResult = UserInfoResponse.of(mockedUser, List.of(mockedHistory));
            given(userService.getUserInfo(anyLong())).willReturn(expectServiceResult);

            mockMvc.perform(
                            get(userInfoSelfUri)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .principal(mockPrincipal))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.message").value("유저 정보 조회 성공"))

                    .andExpect(jsonPath("$.data.id").value("1"))
                    .andExpect(jsonPath("$.data.name").value("TestUser"))
                    .andExpect(jsonPath("$.data.phone").value("01012345678"))
                    .andExpect(jsonPath("$.data.profileImage").value("TestImageUrl"))

                    .andExpect(jsonPath("$.data.activities[0].id").value("1"))
                    .andExpect(jsonPath("$.data.activities[0].generation").value("32"))
                    .andExpect(jsonPath("$.data.activities[0].part").value("SERVER"))
                    .andExpect(jsonPath("$.data.activities[0].team").value("OPERATION"))
                    .andExpect(jsonPath("$.data.activities[0].position").value("MEMBER"));

        }

    }


}