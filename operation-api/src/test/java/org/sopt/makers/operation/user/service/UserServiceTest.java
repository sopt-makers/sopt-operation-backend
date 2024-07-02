package org.sopt.makers.operation.user.service;

import java.util.List;
import java.util.Collections;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.sopt.makers.operation.code.failure.UserFailureCode;
import org.sopt.makers.operation.exception.UserException;
import org.sopt.makers.operation.user.dto.response.UserInfoResponse;
import org.sopt.makers.operation.user.domain.User;
import org.sopt.makers.operation.user.domain.Team;
import org.sopt.makers.operation.user.domain.Position;
import org.sopt.makers.operation.user.domain.UserGenerationHistory;
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
        @DisplayName("Case1. 의도했던 데이터로 구성된 Response 데이터가 반환된다.")
        void expectResponseReturnSuccess() {
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

            Long targetUserId = 1L;
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

        @Test
        @DisplayName("Case. 단일 유저 조회 시에 유효하지 않은 범위의 Id 값일 경우, 예외 반환")
        void throwException_Invalid_Id() {
            // given
            val invalidUserIdZero = 0L;
            val invalidUserIdMinusOne = -1L;

            // when & then
            assertThatThrownBy(() -> userService.getUserInfo(invalidUserIdZero))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserFailureCode.INVALID_USER_INCLUDED.getMessage());
            assertThatThrownBy(() -> userService.getUserInfo(invalidUserIdMinusOne))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserFailureCode.INVALID_USER_INCLUDED.getMessage());
        }

        @Test
        @DisplayName("Case. 복수 유저 조회 시에 유효하지 않은 범위의 Id 값이 포함되어 있을 경우, 예외 반환")
        void throwException_Include_Invalid_Id() {
            // given
            val invalidUserIds = List.of(0L, -1L, 1L);

            // when & then
            assertThatThrownBy(() -> userService.getUserInfos(invalidUserIds))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserFailureCode.INVALID_USER_INCLUDED.getMessage());
        }

    }

}