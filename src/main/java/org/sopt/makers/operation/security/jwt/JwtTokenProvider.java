package org.sopt.makers.operation.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.exception.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
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
                .setExpiration(createAccessExpireDate())
                .signWith(accessKey, SignatureAlgorithm.HS256);

        return jwtBuilder.compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(refreshSecretKey);
        val refreshKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(String.valueOf(authentication.getPrincipal()))
                .setHeader(createHeader())
                .setExpiration(createRefreshExpireDate())
                .signWith(refreshKey, SignatureAlgorithm.HS256);

        return jwtBuilder.compact();
    }

    public AdminAuthentication getAuthentication(String token) {
        AdminAuthentication authentication = new AdminAuthentication(getId(token), null, null);

        return authentication;
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

    public boolean validateAccessTokenExpiration(String token) {
        val now = LocalDateTime.now(KST);

        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(accessSecretKey).build().parseClaimsJws(token);

            if(claims.getBody().getExpiration().toInstant().atZone(KST).toLocalDateTime().isBefore(now)) return false;

            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public boolean validateRefreshTokenExpiration(String token) {
        val now = LocalDateTime.now(KST);

        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(refreshSecretKey).build().parseClaimsJws(token);

            if(claims.getBody().getExpiration().toInstant().atZone(KST).toLocalDateTime().isBefore(now)) return false;

            return true;
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

    private Date createAccessExpireDate() {
        val now = LocalDateTime.now(KST);

        return Date.from(now.plusHours(5).atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date createRefreshExpireDate() {
        val now = LocalDateTime.now(KST);

        return Date.from(now.plusWeeks(2).atZone(ZoneId.systemDefault()).toInstant());
    }
}