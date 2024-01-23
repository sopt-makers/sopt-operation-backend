package org.sopt.makers.operation.security.config;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.security.jwt.JwtAuthenticationFilter;
import org.sopt.makers.operation.security.jwt.JwtExceptionFilter;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${admin.url.local}")
    private String ADMIN_LOCAL_URL;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Profile("dev")
    public SecurityFilterChain filterChainDev(HttpSecurity http) throws Exception {
        setHttp(http);
        authorizeRequestsDev(http);
        return http.build();
    }

    @Profile("dev")
    private void authorizeRequestsDev(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/v1/auth/**", "/exception/**", "/swagger-ui/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/**").authenticated();
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain filterChainProd(HttpSecurity http) throws Exception {
        setHttp(http);
        authorizeRequestsProd(http);
        return http.build();
    }

    @Profile("prod")
    private void authorizeRequestsProd(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/v1/auth/**","/exception/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/**", "/swagger-ui/**").authenticated();
    }

    private void setHttp(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        val configuration = new CorsConfiguration();

        configuration.addAllowedOrigin(ADMIN_PROD_URL);
        configuration.addAllowedOrigin(ADMIN_DEV_URL);
        configuration.addAllowedOrigin(ADMIN_LOCAL_URL);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        val source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
