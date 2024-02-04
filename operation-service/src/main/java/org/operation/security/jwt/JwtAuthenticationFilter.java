package org.operation.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.common.ExceptionMessage;
import org.sopt.makers.operation.exception.TokenException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        val uri = request.getRequestURI();

        if ((uri.startsWith("/api/v1")) && !uri.contains("auth")) {
            val token = jwtTokenProvider.resolveToken(request);

            val jwtTokenType = validateTokenType(request);

            checkJwtAvailable(token, jwtTokenType);

            val auth = jwtTokenProvider.getAuthentication(token, jwtTokenType);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    private void checkJwtAvailable (String token, JwtTokenType jwtTokenType) {
        if (token == null || !jwtTokenProvider.validateTokenExpiration(token, jwtTokenType)) {
            throw new TokenException(ExceptionMessage.INVALID_AUTH_REQUEST.getName());
        }
    }

    private JwtTokenType validateTokenType(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/v1/app") ?
                JwtTokenType.APP_ACCESS_TOKEN :
                JwtTokenType.ACCESS_TOKEN;
    }
}