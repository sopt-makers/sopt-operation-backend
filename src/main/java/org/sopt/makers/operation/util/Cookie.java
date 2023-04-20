package org.sopt.makers.operation.util;

import lombok.val;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;

import org.springframework.http.HttpHeaders;
import java.time.Duration;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Configuration
public class Cookie {
    public HttpHeaders setRefreshToken(String refreshToken) {
        val cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .maxAge(Duration.ofDays(14))
                .secure(true)
                .path("/")
                .build();

        val headers = new org.springframework.http.HttpHeaders();
        headers.add(SET_COOKIE, cookie.toString());

        return headers;
    }
}
