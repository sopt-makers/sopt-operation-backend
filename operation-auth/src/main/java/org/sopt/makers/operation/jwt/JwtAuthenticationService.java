package org.sopt.makers.operation.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.makers.operation.authentication.MakersAuthentication;
import org.sopt.makers.operation.code.failure.auth.JwtFailure;
import org.sopt.makers.operation.exception.JwkException;
import org.sopt.makers.operation.exception.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.text.ParseException;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationService {

    private final JwkProvider jwkProvider;
    private static final String ROLES = "roles";

    @Value("${spring.jwt.jwk.issuer}")
    private String issuer;

    /**
     * JWT 토큰을 검증하고, 사용자 인증 정보를 반환합니다.
     * 내부 동작 순서:
     * 1. JWT 파싱 (header에서 kid 추출)
     * 2. JWK Provider에서 공개키 조회
     * 3. verifyWithRetry를 통해 서명 및 클레임 검증 (재시도 포함)
     * 4. 검증된 JWTClaimsSet으로부터 {@link MakersAuthentication} 생성
     *
     * @param token 서명된 JWT 문자열
     * @return 인증 정보 객체
     * @throws JwtException 서명 검증 실패, 파싱 실패, 키 조회 실패 등의 경우
     */
    public MakersAuthentication authenticate(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            String kid = jwt.getHeader().getKeyID();
            PublicKey key = jwkProvider.getPublicKey(kid);
            JWTClaimsSet claims = verifyWithRetry(jwt, kid, key);

            return toAuthentication(claims);
        } catch (ParseException e) {
            log.warn("JWT 파싱 실패: {}", e.getMessage());
            throw new JwtException(JwtFailure.JWT_PARSE_FAILED);
        } catch (JwkException e) {
            log.warn("JWT 서명 검증 실패: {}", e.getMessage());
            throw new JwtException(JwtFailure.JWT_VERIFICATION_FAILED);
        }
    }

    /**
     * JWTClaimsSet을 기반으로 인증 객체를 생성합니다.
     *
     * @param claims JWT 내부 클레임
     * @return 사용자 인증 정보 객체
     */
    private MakersAuthentication toAuthentication(JWTClaimsSet claims) {
        String userId = claims.getSubject();
        List<String> roles = (List<String>) claims.getClaim(ROLES);
        return new MakersAuthentication(userId, roles);
    }

    /**
     * JWT 검증을 수행하며, 서명 검증 실패 시 1회 재시도를 수행합니다.
     *
     * @param jwt 검증할 JWT 객체
     * @param kid JWT Header의 Key ID
     * @param publicKey 현재 캐시된 공개키
     * @return 검증된 JWTClaimsSet
     */
    private JWTClaimsSet verifyWithRetry(SignedJWT jwt, String kid, PublicKey publicKey) {
        try {
            return verify(jwt, publicKey);
        } catch (JOSEException | ParseException e) {
            log.warn("JWT verification failed. reason={}", e.getMessage());
            return retryVerification(jwt, kid);
        }
    }

    /**
     * JWT 서명 검증 실패 시 캐시된 공개키를 무효화하고,
     * 인증 서버에서 공개키를 다시 가져와 재검증을 시도하는 로직입니다.
     * 이 로직은 다음과 같은 상황에 실행됩니다:
     * - JWK 캐시가 만료되었거나,
     * - 키 롤링(갱신) 등으로 인해 기존 키로는 서명 검증이 실패하는 경우
     * 동작 순서:
     * 1. 기존 키로 검증 시도 → 실패
     * 2. 캐시 무효화 → JWK 재요청
     * 3. 새 키로 검증 재시도
     * 4. 여전히 실패 시 {@link JwtException} 발생
     *
     * @param jwt 서명을 검증할 JWT
     * @param kid JWT 헤더에 포함된 키 ID
     * @return 검증된 JWTClaimsSet
     * @throws JwtException 키 재조회 후에도 검증에 실패한 경우
     */
    private JWTClaimsSet retryVerification(SignedJWT jwt, String kid) {
        jwkProvider.invalidateKey(kid);
        try {
            PublicKey refreshedKey = jwkProvider.getPublicKey(kid);
            return verify(jwt, refreshedKey);
        } catch (JwkException | JOSEException | ParseException e) {
            log.error("Re-verification failed. reason={}", e.getMessage());
            throw new JwtException(JwtFailure.JWT_VERIFICATION_FAILED);
        }
    }

    /**
     * JWT의 서명, 발급자(issuer), 만료 시간(expiration)을 검증합니다.
     *
     * @param jwt        검증할 JWT
     * @param publicKey  서명 검증에 사용할 RSA 공개키
     * @return 검증된 JWTClaimsSet
     * @throws JOSEException 서명 검증 실패 시
     * @throws ParseException JWT 클레임 파싱 실패 시
     * @throws JwtException 클레임 자체가 유효하지 않은 경우 (issuer 불일치, 만료 등)
     */
    private JWTClaimsSet verify(SignedJWT jwt, PublicKey publicKey) throws JOSEException, ParseException {
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
        boolean signatureValid = jwt.verify(verifier);
        boolean issuerValid = issuer.equals(claims.getIssuer());
        boolean notExpired = claims.getExpirationTime().after(new Date());

        if (!(signatureValid && issuerValid && notExpired)) {
            log.warn("Invalid JWT claims detected. signatureValid={}, issuerValid={}, notExpired={}",
                    signatureValid, issuerValid, notExpired);
            throw new JwtException(JwtFailure.JWT_INVALID_CLAIMS);
        }
        return claims;
    }
}