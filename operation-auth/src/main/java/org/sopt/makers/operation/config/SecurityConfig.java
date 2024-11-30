package org.sopt.makers.operation.config;

import lombok.RequiredArgsConstructor;
import lombok.val;
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
    private final ValueConfig valueConfig;

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

    private void setHttp(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(new AntPathRequestMatcher(AUTH_PATH_PATTERN)).permitAll()
                                .requestMatchers(new AntPathRequestMatcher(TEST_PATH_PATTERN)).permitAll()
                                .requestMatchers(new AntPathRequestMatcher(ERROR_PATH_PATTERN)).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
                                .anyRequest().authenticated())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        val configuration = new CorsConfiguration();

        configuration.addAllowedOrigin(valueConfig.getADMIN_PROD_URL());
        configuration.addAllowedOrigin(valueConfig.getADMIN_DEV_URL());
        configuration.addAllowedOrigin(valueConfig.getADMIN_LOCAL_URL());
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        val source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
