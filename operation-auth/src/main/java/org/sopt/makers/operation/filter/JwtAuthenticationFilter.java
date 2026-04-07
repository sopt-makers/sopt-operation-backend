package org.sopt.makers.operation.filter;

import static org.sopt.makers.operation.code.failure.TokenFailureCode.*;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.sopt.makers.operation.authentication.MakersAuthentication;
import org.sopt.makers.operation.exception.TokenException;
import org.sopt.makers.operation.jwt.JwtAuthenticationService;
import org.sopt.makers.operation.jwt.JwtTokenProvider;
import org.sopt.makers.operation.jwt.JwtTokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationService jwtAuthenticationService;

    @Value("${spring.jwt.jwk.issuer}")
    private String Eissuer;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        val uri = request.getRequestURI();
        log.debug("Request URI: {}", uri);

        // Swagger 경로는 JWT 인증 건너뛰기
        if (isSwaggerPath(uri)) {
            chain.doFilter(request, response);
            return;
        }

        if ((uri.startsWith("/api/v1")) && !isExcludedFromJwtAuth(uri)
                && !uri.contains("test") && !isAlarmUpdateRequest(request)
                && !isBannerImageRequest(request)) {
            val token = jwtTokenProvider.resolveToken(request);
            log.debug("Authorization header present: {}", token != null);
            try {
                if (isExternalJwkToken(token)) {
                    MakersAuthentication authentication = jwtAuthenticationService.authenticate(token);
                    authentication.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    val jwtTokenType = validateTokenType(request);
                    log.debug("JWT Token type: {}", jwtTokenType);
                    checkJwtAvailable(token, jwtTokenType);
                    val auth = jwtTokenProvider.getAuthentication(token, jwtTokenType);
                    log.debug("Authentication object created for subject ID: {}", auth.getPrincipal());

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                log.debug("Authentication successful for URI: {}", uri);
            } catch (TokenException e) {
                log.warn("Token validation failed for URI [{}]: {}", uri, e.getMessage());
                throw e;
            } catch (Exception e) {
                log.warn("Authentication failed for URI [{}]: {}", uri, e.getMessage());
                throw new TokenException(INVALID_TOKEN);
            }
        }

        try {
            chain.doFilter(request, response);
            log.debug("Request processing completed for URI: {}", uri);
        } catch (Exception e) {
            log.error("Error occurred during filter chain processing: {}", e.getMessage(), e);
            throw e;
        }
    }

    private boolean isExcludedFromJwtAuth(String uri) {
        return uri.contains("/auth") && !uri.equals("/api/v1/auth/password");
    }

    private boolean isBannerImageRequest(HttpServletRequest request) {
        return request.getMethod().equals("GET") &&
                request.getRequestURI().equals("/api/v1/banners/images");
    }

    private void checkJwtAvailable(String token, JwtTokenType jwtTokenType) {
        log.debug("Checking JWT availability for token type: {}", jwtTokenType);

        if (token == null) {
            log.warn("Token is null for type: {}", jwtTokenType);
            throw new TokenException(INVALID_TOKEN);
        }

        boolean isValid = jwtTokenProvider.validateTokenExpiration(token, jwtTokenType);

        if (!isValid) {
            log.warn("Token expired for type: {}", jwtTokenType);
            throw new TokenException(INVALID_TOKEN);
        }

        log.debug("Token successfully validated for type: {}", jwtTokenType);
    }

    private JwtTokenType validateTokenType(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/v1/app") ?
                JwtTokenType.APP_ACCESS_TOKEN :
                JwtTokenType.ACCESS_TOKEN;
    }

    private boolean isExternalJwkToken(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            String kid = jwt.getHeader().getKeyID();
            String issuer = jwt.getJWTClaimsSet().getIssuer();

            return kid != null && !kid.isEmpty() && Eissuer.equals(issuer);
        } catch (Exception e) {
            log.debug("토큰 파싱 실패, 내부 토큰으로 간주: {}", e.getMessage());
            return false;
        }
    }

    private boolean isAlarmUpdateRequest(HttpServletRequest request) {
        boolean isPatchRequest = request.getMethod().equals("PATCH");
        boolean isAlarmRequest = request.getRequestURI().contains("/api/v1/alarms");
        return isPatchRequest && isAlarmRequest;
    }

    private boolean isSwaggerPath(String uri) {
        return uri.startsWith("/swagger-ui")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/v3/")
                || uri.equals("/swagger-ui.html");
    }
}
