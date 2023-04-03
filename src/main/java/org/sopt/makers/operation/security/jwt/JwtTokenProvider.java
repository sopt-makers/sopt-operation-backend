package org.sopt.makers.operation.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.exception.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final ZoneId KST = ZoneId.of("Asia/Seoul");

    public String generateAccessToken(Authentication authentication) {
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(accessSecretKey);
        val accessKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(String.valueOf(authentication.getPrincipal()))
                .setHeader(createHeader())
                .setExpiration(createExpireDate(JwtTokenType.ACCESS_TOKEN))
                .signWith(accessKey, SignatureAlgorithm.HS256);

        return jwtBuilder.compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(refreshSecretKey);
        val refreshKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(String.valueOf(authentication.getPrincipal()))
                .setHeader(createHeader())
                .setExpiration(createExpireDate(JwtTokenType.REFRESH_TOKEN))
                .signWith(refreshKey, SignatureAlgorithm.HS256);

        return jwtBuilder.compact();
    }

    public AdminAuthentication getAuthentication(String token) {
        return new AdminAuthentication(getId(token), null, null);
    }

    public Long getId(String token) {
        try {
            return Long.parseLong(Jwts.parserBuilder().setSigningKey(accessSecretKey).build().parseClaimsJws(token).getBody().getSubject());
        } catch(ExpiredJwtException e) {
            throw new TokenException("만료된 토큰입니다");
        }
    }

    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("Authorization");
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now(KST);
    }

    private String setSecretKey(JwtTokenType jwtTokenType) {
        return switch (jwtTokenType) {
            case ACCESS_TOKEN -> accessSecretKey;
            case REFRESH_TOKEN -> refreshSecretKey;
        };
    }

    private LocalDateTime setExpireTime(LocalDateTime now, JwtTokenType jwtTokenType) {
        return switch (jwtTokenType) {
            case ACCESS_TOKEN -> now.plusHours(5);
            case REFRESH_TOKEN -> now.plusWeeks(2);
        };
    }

    public boolean validateTokenExpiration(String token, JwtTokenType jwtTokenType) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(setSecretKey(jwtTokenType)).build().parseClaimsJws(token);

            return !claims.getBody().getExpiration().toInstant().atZone(KST).toLocalDateTime().isBefore(getCurrentTime());
        } catch(Exception e) {
            return false;
        }
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }

    private Date createExpireDate(JwtTokenType jwtTokenType) {
        return Date.from(setExpireTime(getCurrentTime(), jwtTokenType).atZone(ZoneId.systemDefault()).toInstant());
    }

}