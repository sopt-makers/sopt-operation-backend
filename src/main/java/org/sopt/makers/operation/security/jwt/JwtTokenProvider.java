package org.sopt.makers.operation.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.entity.Admin;
import org.sopt.makers.operation.exception.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserDetailsService userDetailsService;

    public String generateAccessToken(Admin admin) {
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(accessSecretKey);
        val accessKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(Long.toString(admin.getId()))
                .setHeader(createHeader())
                .setClaims(createClaims(admin))
                .setExpiration(createAccessExpireDate())
                .signWith(accessKey, SignatureAlgorithm.HS256);

        return jwtBuilder.compact();
    }

    public String generateRefreshToken(Admin admin) {
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(refreshSecretKey);
        val refreshKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(Long.toString(admin.getId()))
                .setHeader(createHeader())
                .setClaims(createClaims(admin))
                .setExpiration(createRefreshExpireDate())
                .signWith(refreshKey, SignatureAlgorithm.HS256);

        return jwtBuilder.compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(Long.toString(getId(token)));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public Long getId(String token) {
        try {
            return Long.parseLong(Jwts.parserBuilder().setSigningKey(accessSecretKey).build().parseClaimsJws(token).getBody().get("id").toString());
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

    private Map<String, Object> createClaims(Admin admin) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", admin.getId());
        claims.put("status", admin.getStatus());
        claims.put("role", admin.getRole());

        return claims;
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