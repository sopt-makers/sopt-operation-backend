package org.sopt.makers.operation.auth.service;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.makers.operation.auth.repository.TeamOAuthInfoRepository;
import org.sopt.makers.operation.client.social.SocialLoginManager;
import org.sopt.makers.operation.exception.AuthException;
import org.sopt.makers.operation.user.domain.SocialType;
import org.sopt.makers.operation.user.repository.identityinfo.UserIdentityInfoRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    SocialLoginManager socialLoginManager;
    @Mock
    TeamOAuthInfoRepository teamOAuthInfoRepository;
    @Mock
    UserIdentityInfoRepository userIdentityInfoRepository;

    AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(socialLoginManager, teamOAuthInfoRepository, userIdentityInfoRepository);
    }

    @Nested
    @DisplayName("getSocialUserInfo 메서드 테스트")
    class GetSocialUserInfoMethodTest {
        @DisplayName("socialLoginManager 으로부터 id token 값을 null 값을 받았다면 AuthException 을 반환한다.")
        @Test
        void test() {
            // given
            val socialType = SocialType.APPLE;
            val code = "social code";
            given(socialLoginManager.getIdTokenByCode(socialType, code)).willReturn(null);

            // when, then
            assertThatThrownBy(() -> authService.getSocialUserInfo(socialType, code))
                    .isInstanceOf(AuthException.class)
                    .hasMessage("[AuthException] : 유효하지 않은 social code 입니다.");
        }
    }

    @Nested
    @DisplayName("getUserId 메서드 테스트")
    class GetUserIdMethodTest {
        @DisplayName("사전에 등록되지 않은 social type, social id 가 들어왔을 때, AuthException 을 반환한다.")
        @Test
        void test() {
            // given
            val socialType = SocialType.APPLE;
            val userSocialId = "user social id";
            given(userIdentityInfoRepository.findBySocialTypeAndSocialId(socialType, userSocialId)).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> authService.getUserId(socialType, userSocialId))
                    .isInstanceOf(AuthException.class)
                    .hasMessage("[AuthException] : 등록된 소셜 정보가 없습니다.");
        }
    }

}