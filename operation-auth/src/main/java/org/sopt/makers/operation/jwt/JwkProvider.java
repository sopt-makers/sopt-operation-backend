package org.sopt.makers.operation.jwt;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import lombok.extern.slf4j.Slf4j;

import org.sopt.makers.operation.client.auth.AuthClient;
import org.sopt.makers.operation.code.failure.auth.JwkFailure;
import org.sopt.makers.operation.exception.ClientException;
import org.sopt.makers.operation.exception.JwkException;
import org.springframework.stereotype.Component;


import java.security.PublicKey;
import java.time.Duration;

@Slf4j
@Component
public class JwkProvider {

    private final Cache<String, PublicKey> keyCache;
    private final AuthClient authClient;

    public JwkProvider(AuthClient authClient) {
        this.keyCache = Caffeine.newBuilder()
                .maximumSize(20)
                .expireAfterWrite(Duration.ofDays(1))
                .build();
        this.authClient = authClient;
    }

    /**
     * 주어진 kid에 해당하는 PublicKey 반환
     * @param kid JWT header의 Key ID
     * @return RSA PublicKey
     * @throws JwkException 키 조회 실패 시
     */
    public PublicKey getPublicKey(String kid) {
        return keyCache.get(kid, this::resolvePublicKey);
    }

    private PublicKey resolvePublicKey(String kid) {
        try {
            JWKSet jwkSet = loadJwkSet();
            JWK jwk = findJwkByKeyId(jwkSet, kid);
            return convertToPublicKey(jwk);
        } catch (JwkException | ClientException e) {
            throw e;
        } catch (RuntimeException | IOException | ParseException e) {
            log.error(e.getMessage());
            throw new JwkException(JwkFailure.JWK_FETCH_FAILED);
        }
    }

    private JWKSet loadJwkSet() throws IOException, ParseException, ClientException {
        String json = authClient.getJwk();
        return JWKSet.parse(json);
    }

    private JWK findJwkByKeyId(JWKSet jwkSet, String kid) {
        return jwkSet.getKeys().stream()
                .filter(jwk -> kid.equals(jwk.getKeyID()))
                .findFirst()
                .orElseThrow(() -> new JwkException(JwkFailure.JWK_KID_NOT_FOUND));
    }

    private RSAPublicKey convertToPublicKey(JWK jwk) {
        if (!(jwk instanceof RSAKey rsaKey)) {
            throw new JwkException(JwkFailure.JWK_INVALID_FORMAT);
        }

        try {
            return rsaKey.toRSAPublicKey();
        } catch (JOSEException e) {
            throw new JwkException(JwkFailure.JWK_INVALID_FORMAT);
        }
    }

    /**
     * 캐시 무효화 (예: 복호화 실패 시)
     */
    public void invalidateKey(String kid) {
        log.warn("Invalidating cached JWK for kid={}", kid);
        keyCache.invalidate(kid);
    }
}