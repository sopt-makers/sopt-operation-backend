package org.sopt.makers.operation.security.config;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.security.jwt.JwtAuthenticationFilter;
import org.sopt.makers.operation.security.jwt.JwtExceptionFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Value("${admin.url.prod}")
    private String ADMIN_PROD_URL;

    @Value("${admin.url.dev}")
    private String ADMIN_DEV_URL;

    @Value("${admin.url.prod_legacy}")
    private String ADMIN_PROD_URL_LEGACY;

    @Value("${admin.url.dev_legacy}")
    private String ADMIN_DEV_URL_LEGACY;

    @Value("${admin.url.local}")
    private String ADMIN_LOCAL_URL;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.antMatcher("/**")
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                    .authorizeRequests()
                    .antMatchers("/api/v1/auth/**","/exception/**").permitAll()
                .and()
                    .authorizeRequests()
                    .antMatchers("/api/v1/**", "/swagger-ui/**").authenticated()
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        val configuration = new CorsConfiguration();

        configuration.addAllowedOrigin(ADMIN_PROD_URL);
        configuration.addAllowedOrigin(ADMIN_DEV_URL);
        configuration.addAllowedOrigin(ADMIN_LOCAL_URL);
        configuration.addAllowedOrigin(ADMIN_PROD_URL_LEGACY);
        configuration.addAllowedOrigin(ADMIN_DEV_URL_LEGACY);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        val source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
