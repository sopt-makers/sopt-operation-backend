package org.sopt.makers.operation.user.service;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.stream.Stream;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.sopt.makers.operation.code.failure.UserFailureCode;
import org.sopt.makers.operation.exception.UserException;
import org.sopt.makers.operation.user.domain.*;
import org.sopt.makers.operation.user.dto.request.UserActivityModifyRequest;
import org.sopt.makers.operation.user.dto.request.UserModifyRequest;
import org.sopt.makers.operation.user.dto.response.UserInfoResponse;
import org.sopt.makers.operation.user.repository.UserRepository;
import org.sopt.makers.operation.user.repository.history.UserGenerationHistoryRepository;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("[[ Unit Test ]] - UserService ")
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserGenerationHistoryRepository userGenerationHistoryRepository;
    @InjectMocks
    UserServiceImpl userService;

    @Nested
    @DisplayName("[TEST] 성공한 경우에 대한 테스트")
    class SuccessTest {

        @Test
        @DisplayName("Case. 의도했던 데이터로 구성된 유저 조회 결과 Response 데이터가 반환된다.")
        void expectFindUserResponseReturnSuccess() {
            // given
            User mockedUser = mock(User.class);
            UserGenerationHistory mockedHistory = mock(UserGenerationHistory.class);
            given(mockedUser.getId()).willReturn(1L);
            given(mockedUser.getName()).willReturn("TestUser");
            given(mockedUser.getPhone()).willReturn("01012345678");

            given(mockedHistory.getId()).willReturn(1L);
            given(mockedHistory.getGeneration()).willReturn(32);
            given(mockedHistory.getTeam()).willReturn(Team.OPERATION);
            given(mockedHistory.getPosition()).willReturn(Position.MEMBER);

            given(userRepository.findUserById(1L)).willReturn(mockedUser);
            given(userGenerationHistoryRepository.findAllHistoryByUserId(1L)).willReturn(List.of(mockedHistory));

            long targetUserId = 1L;
            UserInfoResponse expected = UserInfoResponse.of(mockedUser, List.of(mockedHistory));

            // when
            UserInfoResponse response = userService.getUserInfo(targetUserId);

            // then
            assertThat(response).usingRecursiveComparison().isEqualTo(expected);
        }

    }

    @Nested
    @DisplayName("[TEST] 실패한 경우에 대한 테스트")
    class FailureTest {

        @Test
        @DisplayName("Case. 단일 유저 조회 시에 null이 주입할 경우, 예외 반환")
        void throwException_Null_Id() {
            // given
            Long invalidUserId = null;

            // when & then
            assertThatThrownBy(() -> userService.getUserInfo(invalidUserId))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserFailureCode.INVALID_PARAMETER.getMessage());
        }

        @Test
        @DisplayName("Case. 복수 유저 조회 시에 빈 Id 리스트를 주입할 경우, 예외 반환")
        void throwException_Empty_Ids() {
            // given
            List<Long> invalidUserIds = Collections.emptyList();

            // when & then
            assertThatThrownBy(() -> userService.getUserInfos(invalidUserIds))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserFailureCode.INVALID_PARAMETER.getMessage());
        }

        @ParameterizedTest(name = "Invalid User ID ({index}) = {0}")
        @DisplayName("Case. 단일 유저 조회 시에 유효하지 않은 범위의 Id 값일 경우, 예외 반환")
        @ValueSource(longs = {0L, -1L})
        void throwException_Invalid_Id(Long invalidUserId) {
            // when & then
            assertThatThrownBy(() -> userService.getUserInfo(invalidUserId))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserFailureCode.INVALID_USER_IN_USER_LIST_PARAMETER.getMessage());
        }

        @Test
        @DisplayName("Case. 복수 유저 조회 시에 유효하지 않은 범위의 Id 값이 포함되어 있을 경우, 예외 반환")
        void throwException_Include_Invalid_Id() {
            // given
            val invalidUserIds = Arrays.asList(0L, -1L, 1L);

            // when & then
            assertThatThrownBy(() -> userService.getUserInfos(invalidUserIds))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserFailureCode.INVALID_USER_IN_USER_LIST_PARAMETER.getMessage());
        }

        @ParameterizedTest
        @DisplayName("Case. 유저 활동 데이터 변경 시에 임원진이면서 기존 저장되어 있던 Team과 다를 경우")
        @MethodSource("ofActivityUpdateRequests")
        void throwException_Update_Team_Data_Of_Executive_User(
                // given
                UserModifyRequest userModifyRequest
        ) {
            val mockedUser = mock(User.class);
            val mockedExistHistory = mock(UserGenerationHistory.class);

            given(mockedUser.getId()).willReturn(1L);
            given(mockedExistHistory.isExecutive()).willReturn(true);
            given(mockedExistHistory.isBelongTeamTo(Team.OPERATION)).willReturn(false);

            given(userRepository.findUserById(1L)).willReturn(mockedUser);
            given(userGenerationHistoryRepository.findHistoryById(1L)).willReturn(mockedExistHistory);

            // then
            assertThatThrownBy(() -> userService.modifyUserInfo(mockedUser.getId(), userModifyRequest))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserFailureCode.INVALID_USER_MODIFY_ACTIVITY_INFO.getMessage());
        }
        static Stream<Arguments> ofActivityUpdateRequests() {
            return Stream.of(
                    Arguments.of(
//                            new UserPersonalInfoUpdateDao("TestUser",
//                                    "01012345678",
//                                    "TestProfileImage"
//                            ),
                            new UserModifyRequest(
                                    "TestUser",
                                    "01012345678",
                                    "TestProfileImage",
                                    List.of(
                                            new UserActivityModifyRequest(1L, 34, Part.SERVER, Team.OPERATION, Position.TEAM_LEADER)
                                    )
                            )
                    )
            );
        }
    }
}