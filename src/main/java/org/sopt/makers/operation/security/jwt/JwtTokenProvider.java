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
                .setExpiration(createExpireDate("access"))
                .signWith(accessKey, SignatureAlgorithm.HS256);

        return jwtBuilder.compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(refreshSecretKey);
        val refreshKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(String.valueOf(authentication.getPrincipal()))
                .setHeader(createHeader())
                .setExpiration(createExpireDate("refresh"))
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

    private String setSecretKey(String type) {
        return switch (type) {
            case "access" -> accessSecretKey;
            case "refresh" -> refreshSecretKey;
            default -> throw new TokenException("잘못된 유형의 토큰입니다");
        };
    }

    private LocalDateTime setExpireTime(LocalDateTime now, String type) {
        return switch (type) {
            case "access" -> now.plusHours(5);
            case "refresh" -> now.plusWeeks(2);
            default -> throw new TokenException("잘못된 유형의 토큰입니다");
        };
    }

    public boolean validateTokenExpiration(String token, String type) {
        String secretKey = setSecretKey(type);

        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

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

    private Date createExpireDate(String type) {
        return Date.from(setExpireTime(getCurrentTime(), type).atZone(ZoneId.systemDefault()).toInstant());
    }

}