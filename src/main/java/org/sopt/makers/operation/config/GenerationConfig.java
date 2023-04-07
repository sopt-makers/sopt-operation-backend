package org.sopt.makers.operation.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration
public class GenerationConfig {
    @Value("${sopt.current.generation}")
    private int currentGeneration;
}
