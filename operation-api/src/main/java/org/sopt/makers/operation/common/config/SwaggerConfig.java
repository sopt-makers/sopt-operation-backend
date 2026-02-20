package org.sopt.makers.operation.common.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "JWT",
        scheme = "Bearer"
)
@Configuration
public class SwaggerConfig {

    // ✨ 기존 dev/prod용
    @Bean
    @Profile({"dev", "prod"})
    public OpenAPI api() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Localhost Server");

        Server httpsServer = new Server()
                .url("https://operation.api.dev.sopt.org")
                .description("HTTPS Server");

        return createOpenAPI(List.of(localServer, httpsServer));
    }

    // ✨ Lambda Dev용 - API Gateway URL 사용
    @Bean
    @Profile("lambda-dev")
    public OpenAPI apiLambdaDev() {
        Server lambdaServer = new Server()
                .url("https://operation-api-dev.sopt.org")  // ✨ 커스텀 도메인으로 변경
                .description("Lambda Dev Server");

        return createOpenAPI(List.of(lambdaServer));
    }

    // ✨ Lambda Prod용
    @Bean
    @Profile("lambda-prod")
    public OpenAPI apiLambdaProd() {
        Server httpsServer = new Server()
                .url("https://operation.api.sopt.org")
                .description("Production Server");

        return createOpenAPI(List.of(httpsServer));
    }

    // ✨ 공통 OpenAPI 생성 메서드
    private OpenAPI createOpenAPI(List<Server> servers) {
        Info info = new Info()
                .title("Makers Operation API Docs")
                .version("v2.0")
                .description("운영 프로덕트 API 명세서 입니다.");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Authorization");

        return new OpenAPI()
                .info(info)
                .servers(servers)
                .addSecurityItem(securityRequirement);
    }
}
