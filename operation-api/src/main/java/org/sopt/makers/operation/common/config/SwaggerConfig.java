package org.sopt.makers.operation.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SecurityScheme(
		name = "Authorization",
		type = SecuritySchemeType.HTTP,
		in = SecuritySchemeIn.HEADER,
		bearerFormat = "JWT",
		scheme = "Bearer"
)
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI api() {
		Info info = new Info()
				.title("SOPT Makers API Docs")
				.version("v1.0.0")
				.description("SOPT Makers API 명세서");
		return new OpenAPI()
				.info(info);
	}
}
