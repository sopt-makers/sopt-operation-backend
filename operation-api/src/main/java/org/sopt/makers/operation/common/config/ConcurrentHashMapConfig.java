package org.sopt.makers.operation.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ConcurrentHashMapConfig {
    @Bean
    public ConcurrentHashMap<String, String> registerConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }
}
