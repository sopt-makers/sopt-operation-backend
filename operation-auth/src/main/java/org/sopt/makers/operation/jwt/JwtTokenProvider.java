package org.sopt.makers.operation.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.authentication.AdminAuthentication;
import org.sopt.makers.operation.exception.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.sopt.makers.operation.code.failure.TokenFailureCode.INVALID_TOKEN;

@Service
public class JwtTokenProvider {

    private final String accessSecretKey;
    private final String refreshSecretKey;
    private final String appAccessSecretKey;
    private final String platformCodeSecretKey;

    public JwtTokenProvider(
            @Value("${spring.jwt.secretKey.access}") String accessSecretKey,
            @Value("${spring.jwt.secretKey.refresh}") String refreshSecretKey,
            @Value("${spring.jwt.secretKey.app}") String appAccessSecretKey,
            @Value("${spring.jwt.secretKey.platform_code}") String platformCodeSecretKey
    ) {
        this.accessSecretKey = accessSecretKey;
        this.refreshSecretKey = refreshSecretKey;
        this.appAccessSecretKey = appAccessSecretKey;
        this.platformCodeSecretKey = platformCodeSecretKey;
    }

    public String generatePlatformCode(final String clientId, final String redirectUri, final Long userId) {
        val encodeKey = encodeKey(platformCodeSecretKey);
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(encodeKey);
        val signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setHeader(createHeader())
                .setIssuer(clientId)
                .setAudience(redirectUri)
                .setSubject(Long.toString(userId))
                .setExpiration(createExpireDate(JwtTokenType.PLATFORM_CODE))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(final Authentication authentication) {
        val encodedKey = encodeKey(accessSecretKey);
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(encodedKey);
        val accessKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setSubject(String.valueOf(authentication.getPrincipal()))
                .setHeader(createHeader())
                .setExpiration(createExpireDate(JwtTokenType.ACCESS_TOKEN))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(final Authentication authentication) {
        val encodedKey = encodeKey(refreshSecretKey);
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(encodedKey);
        val refreshKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setSubject(String.valueOf(authentication.getPrincipal()))
                .setHeader(createHeader())
                .setExpiration(createExpireDate(JwtTokenType.REFRESH_TOKEN))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateTokenExpiration(String token, JwtTokenType jwtTokenType) {
        try {
            getClaimsFromToken(token, jwtTokenType);
            return true;
        } catch (ExpiredJwtException | SignatureException e) {
            return false;
        }
    }

    public boolean validatePlatformCode(String platformCode, String clientId, String redirectUri) {
        try {
            val claims = getClaimsFromToken(platformCode, JwtTokenType.PLATFORM_CODE);
            return isClaimsMatchingRequest(claims, clientId, redirectUri);
        } catch (ExpiredJwtException | SignatureException e) {
            return false;
        }
    }

    public AdminAuthentication getAuthentication(String token, JwtTokenType jwtTokenType) {
        return switch (jwtTokenType) {
            case ACCESS_TOKEN, REFRESH_TOKEN, PLATFORM_CODE ->
                    new AdminAuthentication(getId(token, jwtTokenType), null, null);
            case APP_ACCESS_TOKEN -> new AdminAuthentication(getPlayGroundId(token, jwtTokenType), null, null);
        };
    }

    public Long getPlayGroundId(String token, JwtTokenType jwtTokenType) {
        try {
            val claims = getClaimsFromToken(token, jwtTokenType);

            return Long.parseLong(claims.get("playgroundId").toString());
        } catch (ExpiredJwtException | SignatureException e) {
            throw new TokenException(INVALID_TOKEN);
        }
    }

    public Long getId(String token, JwtTokenType jwtTokenType) {
        try {
            val claims = getClaimsFromToken(token, jwtTokenType);

            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException | SignatureException e) {
            throw new TokenException(INVALID_TOKEN);
        }
    }

    private boolean isClaimsMatchingRequest(Claims claims, String clientId, String redirectUri) {
        return claims.getAudience().equals(redirectUri)
                && claims.getIssuer().equals(clientId);
    }

    private Claims getClaimsFromToken(String token, JwtTokenType jwtTokenType) {
        val encodedKey = encodeKey(setSecretKey(jwtTokenType));

        return Jwts.parserBuilder()
                .setSigningKey(encodedKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        val headerAuth = request.getHeader("Authorization");
        return (StringUtils.hasText(headerAuth)) ? headerAuth : null;
    }

    private String encodeKey(String secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    private String setSecretKey(JwtTokenType jwtTokenType) {
        return switch (jwtTokenType) {
            case ACCESS_TOKEN -> accessSecretKey;
            case REFRESH_TOKEN -> refreshSecretKey;
            case APP_ACCESS_TOKEN -> appAccessSecretKey;
            case PLATFORM_CODE -> platformCodeSecretKey;
        };
    }

    private LocalDateTime setExpireTime(LocalDateTime now, JwtTokenType jwtTokenType) {
        return switch (jwtTokenType) {
            case ACCESS_TOKEN -> now.plusHours(5);
            case REFRESH_TOKEN -> now.plusWeeks(2);
            case APP_ACCESS_TOKEN -> throw new TokenException(INVALID_TOKEN);
            case PLATFORM_CODE -> now.plusMinutes(5);
        };
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }

    private Date createExpireDate(JwtTokenType jwtTokenType) {
        val now = getCurrentTime();
        val expireTime = setExpireTime(now, jwtTokenType);

        return Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}