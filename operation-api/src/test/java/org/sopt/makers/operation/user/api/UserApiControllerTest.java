package org.sopt.makers.operation.user.api;

import java.security.Principal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import org.sopt.makers.operation.user.domain.User;
import org.sopt.makers.operation.user.domain.Part;
import org.sopt.makers.operation.user.domain.Team;
import org.sopt.makers.operation.user.domain.Position;
import org.sopt.makers.operation.user.domain.UserGenerationHistory;
import org.sopt.makers.operation.user.dto.response.UserInfoResponse;
import org.sopt.makers.operation.user.dto.response.UserInfosResponse;
import org.sopt.makers.operation.user.service.UserService;
import org.sopt.makers.operation.jwt.JwtTokenProvider;
import org.sopt.makers.operation.common.util.CommonUtils;
import org.sopt.makers.operation.common.handler.ErrorHandler;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Import({UserApiController.class})
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(
        classes = {UserApiController.class, ErrorHandler.class}
)
@WebMvcTest(
        controllers = {UserApiController.class},
        excludeAutoConfiguration =  {SecurityAutoConfiguration.class}
)
@DisplayName("[[ Unit Test ]] - User Controller ")
class UserApiControllerTest {

    final String userInfoSelfUri = "/api/v1/internal/user/me";
    final String userInfosUri = "/api/v1/internal/user";

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
        @DisplayName("Case. 요청자 자신에 대한 유저 정보 조회 성공 - /internal/user/me")
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

        @Test
        @DisplayName("Case. 복수 유저 정보 조회 성공 - /internal/user?userIds=")
        void successUserInfos() throws Exception {
            User mockedUserA = mock(User.class);
            UserGenerationHistory mockedHistoryA = mock(UserGenerationHistory.class);

            given(mockedUserA.getId()).willReturn(1L);
            given(mockedUserA.getName()).willReturn("TestUserA");
            given(mockedUserA.getPhone()).willReturn("01012345678");
            given(mockedUserA.getProfileImage()).willReturn("TestImageUrlA");
            given(mockedHistoryA.getId()).willReturn(1L);
            given(mockedHistoryA.getPart()).willReturn(Part.SERVER);
            given(mockedHistoryA.getGeneration()).willReturn(32);
            given(mockedHistoryA.getTeam()).willReturn(Team.OPERATION);
            given(mockedHistoryA.getPosition()).willReturn(Position.MEMBER);

            User mockedUserB = mock(User.class);
            UserGenerationHistory mockedHistoryB = mock(UserGenerationHistory.class);
            given(mockedUserB.getId()).willReturn(2L);
            given(mockedUserB.getName()).willReturn("TestUserB");
            given(mockedUserB.getPhone()).willReturn("01056789012");
            given(mockedUserB.getProfileImage()).willReturn("TestImageUrlB");
            given(mockedHistoryB.getId()).willReturn(2L);
            given(mockedHistoryB.getPart()).willReturn(Part.PLAN);
            given(mockedHistoryB.getGeneration()).willReturn(32);
            given(mockedHistoryB.getTeam()).willReturn(Team.MEDIA);
            given(mockedHistoryB.getPosition()).willReturn(Position.TEAM_LEADER);

            Principal mockPrincipal = mock(Principal.class);
            given(mockPrincipal.getName()).willReturn("1");

            UserInfoResponse expectServiceResultOfA = UserInfoResponse.of(mockedUserA, List.of(mockedHistoryA));
            UserInfoResponse expectServiceResultOfB = UserInfoResponse.of(mockedUserB, List.of(mockedHistoryB));
            UserInfosResponse expected = UserInfosResponse.of(List.of(expectServiceResultOfA, expectServiceResultOfB));

            given(userService.getUserInfos(anyList())).willReturn(expected);

            mockMvc.perform(
                            get(userInfosUri)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .principal(mockPrincipal)
                                    .queryParam("userIds","1%2C2"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.message").value("전체 유저 정보 조회 성공"))

                    .andExpect(jsonPath("$.data.users[0].id").value("1"))
                    .andExpect(jsonPath("$.data.users[0].name").value("TestUserA"))
                    .andExpect(jsonPath("$.data.users[0].phone").value("01012345678"))
                    .andExpect(jsonPath("$.data.users[0].profileImage").value("TestImageUrlA"))

                    .andExpect(jsonPath("$.data.users[0].activities[0].id").value("1"))
                    .andExpect(jsonPath("$.data.users[0].activities[0].generation").value("32"))
                    .andExpect(jsonPath("$.data.users[0].activities[0].part").value("SERVER"))
                    .andExpect(jsonPath("$.data.users[0].activities[0].team").value("OPERATION"))
                    .andExpect(jsonPath("$.data.users[0].activities[0].position").value("MEMBER"))

                    .andExpect(jsonPath("$.data.users[1].id").value("2"))
                    .andExpect(jsonPath("$.data.users[1].name").value("TestUserB"))
                    .andExpect(jsonPath("$.data.users[1].phone").value("01056789012"))
                    .andExpect(jsonPath("$.data.users[1].profileImage").value("TestImageUrlB"))

                    .andExpect(jsonPath("$.data.users[1].activities[0].id").value("2"))
                    .andExpect(jsonPath("$.data.users[1].activities[0].generation").value("32"))
                    .andExpect(jsonPath("$.data.users[1].activities[0].part").value("PLAN"))
                    .andExpect(jsonPath("$.data.users[1].activities[0].team").value("MEDIA"))
                    .andExpect(jsonPath("$.data.users[1].activities[0].position").value("TEAM_LEADER"));

        }

    }

    @Nested
    @DisplayName("[TEST] 실패한 경우에 대한 테스트")
    class FailTest {

        @Test
        @DisplayName("Case. 잘못된 형식의 userIds Parameter 값 : Null - /internal/user?userIds=")
        void failUserInfosCausedByNullParameter() throws Exception {
            Principal mockPrincipal = mock(Principal.class);
            given(mockPrincipal.getName()).willReturn("1");

            mockMvc.perform(
                            get(userInfosUri)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .principal(mockPrincipal)
                                    .param("userIds", ""))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @DisplayName("Case. 잘못된 형식의 userIds Parameter 값 : Wrong Delimiter - /internal/user?userIds=")
        @ParameterizedTest(name = "* {index}번째 입력값 = {0}")
        @ValueSource(strings = {"123/124", "123_124", "123:124", "123-124"})
        void failUserInfosCausedByWrongDelimiter(String wrongDelimiter) throws Exception {
            Principal mockPrincipal = mock(Principal.class);
            given(mockPrincipal.getName()).willReturn("1");

            mockMvc.perform(
                            get(userInfosUri)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .principal(mockPrincipal)
                                    .param("userIds", wrongDelimiter))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @DisplayName("Case. 잘못된 형식의 userIds Parameter 값 : Not Number - /internal/user?userIds=")
        @ParameterizedTest(name = "* {index}번째 입력값 = {0}")
        @ValueSource(strings = {"abc,def", "ㄱㄴㄷ,ㄹㅁㅂ", "[][],.;.;"})
        void failUserInfosCausedByNotNumber(String notNumberValue) throws Exception {
            Principal mockPrincipal = mock(Principal.class);
            given(mockPrincipal.getName()).willReturn("1");

            mockMvc.perform(
                            get(userInfosUri)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .principal(mockPrincipal)
                                    .param("userIds", notNumberValue))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }
}