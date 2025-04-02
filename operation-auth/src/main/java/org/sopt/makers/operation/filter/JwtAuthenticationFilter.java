package org.sopt.makers.operation.filter;

import static org.sopt.makers.operation.code.failure.TokenFailureCode.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.sopt.makers.operation.exception.TokenException;
import org.sopt.makers.operation.jwt.JwtTokenProvider;
import org.sopt.makers.operation.jwt.JwtTokenType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j  // 로깅을 위한 어노테이션 추가
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        val uri = request.getRequestURI();
        log.info("Request URI: {}", uri);  // URI 로깅
        if ((uri.startsWith("/api/v1")) && !uri.contains("auth") && !uri.contains("test") && !isAlarmUpdateRequest(request)) {
            val token = jwtTokenProvider.resolveToken(request);
            log.info("Authorization header: {}", token);  // 토큰 로깅

            try {
                val jwtTokenType = validateTokenType(request);
                log.info("JWT Token type: {}", jwtTokenType);  // 토큰 타입 로깅

                checkJwtAvailable(token, jwtTokenType);

                val auth = jwtTokenProvider.getAuthentication(token, jwtTokenType);
                log.info("Authentication object created for subject ID: {}", auth.getPrincipal());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Authentication successful");  // 인증 성공 로깅
            } catch (TokenException e) {
                log.error("Token exception occurred: {}", e.getMessage(), e);
                throw e;
            }catch (Exception e) {
                log.error("Authentication error: {}", e.getMessage(), e);  // 인증 에러 로깅
                throw new TokenException(INVALID_TOKEN);
            }
        }

        try {
            chain.doFilter(request, response);
            log.info("Request processing completed for URI: {}", uri);
        } catch (Exception e) {
            log.error("Error occurred during filter chain processing: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void checkJwtAvailable(String token, JwtTokenType jwtTokenType) {
        log.info("Checking JWT availability for token type: {}", jwtTokenType);

        if (token == null) {
            log.error("Token is null - Authentication failed");
            throw new TokenException(INVALID_TOKEN);
        }

        boolean isValid = jwtTokenProvider.validateTokenExpiration(token, jwtTokenType);

        if (!isValid) {
            log.error("Token validation failed for token type: {}", jwtTokenType);
            throw new TokenException(INVALID_TOKEN);
        }

        log.info("Token successfully validated for type: {}", jwtTokenType);
    }

    private JwtTokenType validateTokenType(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/v1/app") ?
                JwtTokenType.APP_ACCESS_TOKEN :
                JwtTokenType.ACCESS_TOKEN;
    }

    private boolean isAlarmUpdateRequest(HttpServletRequest request) {
        boolean isPatchRequest = request.getMethod().equals("PATCH");
        boolean isAlarmRequest = request.getRequestURI().contains("/api/v1/alarms");
        return isPatchRequest && isAlarmRequest;
    }
}