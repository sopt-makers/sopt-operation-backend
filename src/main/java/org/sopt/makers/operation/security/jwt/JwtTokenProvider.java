package org.sopt.makers.operation.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.common.ExceptionMessage;
import org.sopt.makers.operation.exception.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class JwtTokenProvider {

    @Value("${spring.jwt.secretKey.access}")
    private String accessSecretKey;

    @Value("${spring.jwt.secretKey.refresh}")
    private String refreshSecretKey;

    @Value("${spring.jwt.secretKey.app}")
    private String appAccessSecretKey;

    public String generateAccessToken(Authentication authentication) {
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

    public String generateRefreshToken(Authentication authentication) {
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
        } catch (ExpiredJwtException e) {
            throw new TokenException(ExceptionMessage.EXPIRED_TOKEN.getName());
        } catch (SignatureException e) {
            throw new TokenException(ExceptionMessage.INVALID_SIGNATURE.getName());
        }
    }

    public AdminAuthentication getAuthentication(String token, JwtTokenType jwtTokenType) {
        return switch (jwtTokenType) {
            case ACCESS_TOKEN, REFRESH_TOKEN -> new AdminAuthentication(getId(token, jwtTokenType), null, null);
            case APP_ACCESS_TOKEN -> new AdminAuthentication(getPlayGroundId(token, jwtTokenType), null, null);
        };
    }

    public Long getPlayGroundId(String token, JwtTokenType jwtTokenType) {
        try {
            val claims = getClaimsFromToken(token, jwtTokenType);

            return Long.parseLong(claims.get("playgroundId").toString());
        } catch (ExpiredJwtException e) {
            throw new TokenException(ExceptionMessage.EXPIRED_TOKEN.getName());
        } catch (SignatureException e) {
            throw new TokenException(ExceptionMessage.INVALID_SIGNATURE.getName());
        }
    }

    public Long getId(String token, JwtTokenType jwtTokenType) {
        try {
            val claims = getClaimsFromToken(token, jwtTokenType);

            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException e) {
            throw new TokenException(ExceptionMessage.EXPIRED_TOKEN.getName());
        } catch (SecurityException e) {
            throw new TokenException(ExceptionMessage.INVALID_SIGNATURE.getName());
        }
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
        };
    }

    private LocalDateTime setExpireTime(LocalDateTime now, JwtTokenType jwtTokenType) {
        return switch (jwtTokenType) {
            case ACCESS_TOKEN -> now.plusMinutes(3);
            case REFRESH_TOKEN -> now.plusWeeks(2);
            case APP_ACCESS_TOKEN -> throw new TokenException(ExceptionMessage.INVALID_TOKEN.getName());
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