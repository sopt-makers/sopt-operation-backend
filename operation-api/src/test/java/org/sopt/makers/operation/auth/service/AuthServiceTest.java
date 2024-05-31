package org.sopt.makers.operation.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.xml.bind.DatatypeConverter;
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
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.AuthException;
import org.sopt.makers.operation.user.domain.SocialType;
import org.sopt.makers.operation.user.repository.identityinfo.UserIdentityInfoRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    @Mock
    ValueConfig valueConfig;

    AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(socialLoginManager, teamOAuthInfoRepository, userIdentityInfoRepository, valueConfig);
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

    @Nested
    @DisplayName("generatePlatformCode 메서드 테스트")
    class GeneratePlatformCodeMethodTest {
        final String platformSecretKey = "123456789123456789123456789123456789123456789123456789";

        @DisplayName("iss:clientId, aud:redirectUri , sub:userId 인 jwt 토큰을 발급한다.")
        @Test
        void test() {
            // given
            val clientId = "clientId";
            val redirectUri = "redirectUri";
            val userId = 1L;
            given(valueConfig.getPlatformCodeSecretKey()).willReturn(platformSecretKey);

            // when
            String platformCode = authService.generatePlatformCode(clientId, redirectUri, userId);
            Claims claims = getClaimsFromToken(platformCode);

            // then
            assertThat(claims.getIssuer()).isEqualTo("clientId");
            assertThat(claims.getAudience()).isEqualTo("redirectUri");
            assertThat(claims.getSubject()).isEqualTo("1");
        }

        private Claims getClaimsFromToken(String token) throws SignatureException {
            return Jwts.parserBuilder()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(platformSecretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
    }
}