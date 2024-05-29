package org.sopt.makers.operation.client.social;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.sopt.makers.operation.client.social.dto.IdTokenResponse;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.AuthException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.FAILURE_READ_PRIVATE_KEY;

@Component
@RequiredArgsConstructor
public class AppleSocialLogin {
    private final RestTemplate restTemplate;
    private final ValueConfig valueConfig;

    public IdTokenResponse getIdTokenByCode(String code) {
        val tokenRequest = new LinkedMultiValueMap<>();
        val grantType = "authorization_code";
        val clientId = valueConfig.getAppleSub();
        val clientSecret = createClientSecret();

        tokenRequest.add("client_id", clientId);
        tokenRequest.add("client_secret", clientSecret);
        tokenRequest.add("code", code);
        tokenRequest.add("grant_type", grantType);

        val headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        val entity = new HttpEntity<>(tokenRequest, headers);

        val host = "https://appleid.apple.com/auth/token";
        return restTemplate.postForObject(host, entity, IdTokenResponse.class);
    }

    private String createClientSecret() {
        val now = new Date();
        val privateKey = getPrivateKey()
                .orElseThrow(() -> new AuthException(FAILURE_READ_PRIVATE_KEY));
        val kid = valueConfig.getAppleKeyId();
        val issuer = valueConfig.getAppleTeamId();
        val aud = valueConfig.getAppleAud();
        val sub = valueConfig.getAppleSub();

        return Jwts.builder()
                .setHeaderParam("kid", kid)
                .setHeaderParam("alg", "ES256")
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .setIssuer(issuer)
                .setAudience(aud)
                .setSubject(sub)
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();
    }

    private Optional<PrivateKey> getPrivateKey() {
        val appleKeyPath = "authConfig.getAppleKeyPath()";
        try {
            val resource = new ClassPathResource(appleKeyPath);
            val privateKey = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            val pemReader = new StringReader(privateKey);
            val pemParser = new PEMParser(pemReader);
            val converter = new JcaPEMKeyConverter();
            val object = (PrivateKeyInfo) pemParser.readObject();
            return Optional.of(converter.getPrivateKey(object));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

}
