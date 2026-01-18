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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

    // ✨ CORS 설정 빈 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(
                "https://*.sopt.org",
                "http://localhost:*"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "api-key"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
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

    @Bean
    @Profile("lambda-dev")
    public SecurityFilterChain filterChainLambdaDev(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/v3/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/**", "OPTIONS")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/v1/banners/images", "GET")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/v1/banners/images", "OPTIONS")).permitAll()
        );
        setHttpLambda(http);  // ✨ Lambda용 메서드 호출
        return http.build();
    }

    @Bean
    @Profile("lambda-prod")
    public SecurityFilterChain filterChainLambdaProd(HttpSecurity http) throws Exception {
        setHttpLambda(http);  // ✨ Lambda용 메서드 호출
        return http.build();
    }

    // 기존 EC2용 (Nginx가 CORS 처리)
    private void setHttp(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .cors().disable()  // EC2는 Nginx가 처리하니까 그대로
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

    // ✨ Lambda용 (Spring이 CORS 처리)
    private void setHttpLambda(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .cors()  // ✨ CORS 활성화! (corsConfigurationSource 빈 자동 사용)
                .and()
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
