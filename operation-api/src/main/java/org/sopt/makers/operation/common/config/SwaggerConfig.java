package org.sopt.makers.operation.common.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Localhost Server");

        Server httpsServer = new Server()
                .url("https://operation.api.dev.sopt.org")
                .description("HTTPS Server");

        Info info = new Info()
                .title("Makers Operation API Docs")
                .version("v2.0")
                .description("운영 프로덕트 API 명세서 입니다.");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Authorization");



        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, httpsServer))
                .addSecurityItem(securityRequirement);
    }
}
