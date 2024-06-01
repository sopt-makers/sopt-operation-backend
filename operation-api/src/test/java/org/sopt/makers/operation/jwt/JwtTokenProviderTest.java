package org.sopt.makers.operation.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JwtTokenProviderTest {
    final String platformSecretKey = "123456789123456789123456789123456789123456789123456789";
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider("", "", "", platformSecretKey);
    }

    @Nested
    @DisplayName("generatePlatformCode 메서드 테스트")
    class GeneratePlatformCodeMethodTest {

        @DisplayName("iss:clientId, aud:redirectUri , sub:userId 인 jwt 토큰을 발급한다.")
        @Test
        void test() {
            // given
            val clientId = "clientId";
            val redirectUri = "redirectUri";
            val userId = 1L;

            // when
            String platformCode = jwtTokenProvider.generatePlatformCode(clientId, redirectUri, userId);
            val claims = getClaimsFromToken(platformCode);

            // then
            assertThat(claims.getIssuer()).isEqualTo("clientId");
            assertThat(claims.getAudience()).isEqualTo("redirectUri");
            assertThat(claims.getSubject()).isEqualTo("1");
        }

        private Claims getClaimsFromToken(String token) {
            val encodedKey = encodeKey(platformSecretKey);

            return Jwts.parserBuilder()
                    .setSigningKey(encodedKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        private String encodeKey(String secretKey) {
            return Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Nested
    @DisplayName("validatePlatformCode 메서드 테스트")
    class ValidatePlatformCodeMethodTest {

        @DisplayName("iss:clientId, aud:redirectUri , sub:userId 인 jwt 토큰과 clientId, redirectUri 를 아규먼트로 넣으면 true 를 반환한다.")
        @Test
        void test() {
            // given
            val clientId = "clientId";
            val redirectUri = "redirectUri";
            val userId = 1L;
            String platformCode = jwtTokenProvider.generatePlatformCode(clientId, redirectUri, userId);

            // when
            val result = jwtTokenProvider.validatePlatformCode(platformCode, "clientId", "redirectUri");

            // then
            assertThat(result).isTrue();
        }

        @DisplayName("iss:clientId, aud:redirectUri , sub:userId 인 jwt 토큰과 notClientId, notRedirectUri 를 아규먼트로 넣으면 false 를 반환한다.")
        @Test
        void test2() {
            // given
            val clientId = "clientId";
            val redirectUri = "redirectUri";
            val userId = 1L;
            String platformCode = jwtTokenProvider.generatePlatformCode(clientId, redirectUri, userId);

            // when
            val result = jwtTokenProvider.validatePlatformCode(platformCode, "notClientId", "notRedirectUri");

            // then
            assertThat(result).isFalse();
        }
    }

}