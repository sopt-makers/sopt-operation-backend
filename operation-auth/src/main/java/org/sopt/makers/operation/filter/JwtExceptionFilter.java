package org.sopt.makers.operation.filter;

import static org.sopt.makers.operation.code.failure.admin.AdminFailureCode.*;

import org.sopt.makers.operation.code.failure.admin.AdminFailureCode;
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
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {
       try {
           filterChain.doFilter(httpServletRequest, httpServletResponse);
       } catch(TokenException e) {
           val objectMapper = new ObjectMapper();
           val jsonResponse = objectMapper.writeValueAsString(getFailureResponse());

           httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
           httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
           httpServletResponse.setCharacterEncoding("UTF-8");
           httpServletResponse.getWriter().write(jsonResponse);
       }
    }

    private ResponseEntity<BaseResponse<?>> getFailureResponse() {
        return ApiResponseUtil.failure(INVALID_TOKEN);
    }
}