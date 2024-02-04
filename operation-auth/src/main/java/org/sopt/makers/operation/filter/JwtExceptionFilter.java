package org.sopt.makers.operation.filter;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.val;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {
//        try {
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//        } catch(TokenException e) {
//            val objectMapper = new ObjectMapper();
//            //val jsonResponse = objectMapper.writeValueAsString(ResponseDTO.fail(e.getMessage()));
//
//            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
//            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            httpServletResponse.setCharacterEncoding("UTF-8");
//            //httpServletResponse.getWriter().write(jsonResponse);
//        }
    }
}