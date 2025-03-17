package org.sopt.makers.operation.filter;

import lombok.extern.slf4j.Slf4j;
import org.sopt.makers.operation.code.failure.FailureCode;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.exception.TokenException;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {
       try {
           filterChain.doFilter(httpServletRequest, httpServletResponse);
       } catch(TokenException e) {
           log.error("Token Exception caught: {}", e.getMessage(), e);  // 토큰 예외 로깅
           val objectMapper = new ObjectMapper();
           val jsonResponse = objectMapper.writeValueAsString(getFailureResponse(e.getFailureCode()));

           httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
           httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
           httpServletResponse.setCharacterEncoding("UTF-8");
           httpServletResponse.getWriter().write(jsonResponse);
       }
    }

    private ResponseEntity<BaseResponse<?>> getFailureResponse(FailureCode failureCode) {
        return ApiResponseUtil.failure(failureCode);
    }
}