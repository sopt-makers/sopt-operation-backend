package org.sopt.makers.operation.config;

import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.filter.JwtAuthenticationFilter;
import org.sopt.makers.operation.filter.JwtExceptionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private static final String API_V1_PREFIX = "/api/v1";
    private static final String AUTH_PATH_PATTERN = API_V1_PREFIX + "/auth/**";
    private static final String TEST_PATH_PATTERN = API_V1_PREFIX + "/test/**";
    private static final String ERROR_PATH_PATTERN = "/error";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Profile("dev")
    public SecurityFilterChain filterChainDev(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/v3/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/v1/banners/images", "GET")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/v1/banners/images", "OPTIONS")).permitAll()
        );
        setHttp(http);
        return http.build();
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain filterChainProd(HttpSecurity http) throws Exception {
        setHttp(http);
        return http.build();
    }

    // ✨ Lambda Dev 환경용 SecurityFilterChain 추가
    @Bean
    @Profile("lambda-dev")
    public SecurityFilterChain filterChainLambdaDev(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/v3/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/v1/banners/images", "GET")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/v1/banners/images", "OPTIONS")).permitAll()
        );
        setHttp(http);
        return http.build();
    }

    // ✨ Lambda Prod 환경용 SecurityFilterChain 추가
    @Bean
    @Profile("lambda-prod")
    public SecurityFilterChain filterChainLambdaProd(HttpSecurity http) throws Exception {
        setHttp(http);
        return http.build();
    }

    private void setHttp(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .cors().disable()
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(new AntPathRequestMatcher(AUTH_PATH_PATTERN)).permitAll()
                                .requestMatchers(new AntPathRequestMatcher(TEST_PATH_PATTERN)).permitAll()
                                .requestMatchers(new AntPathRequestMatcher(ERROR_PATH_PATTERN)).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/alarms/**", "PATCH")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/banners/images", "GET")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
                                .anyRequest().authenticated())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
    }

}
