package org.sopt.makers.operation.config;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.sopt.makers.operation.client.auth.AuthClientProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


import java.time.Duration;

@Configuration
public class AuthWebClientConfig {

    public static final String HEADER_API_KEY = "X-Api-Key";
    public static final String HEADER_SERVICE_NAME = "X-Service-Name";
    private static final int TIMEOUT_MILLIS = 5000;
    private static final int TIMEOUT_SECONDS = 5;

    @Bean
    public WebClient authWebClient(AuthClientProperty property) {
        return WebClient.builder()
                .baseUrl(property.url())
                .clientConnector(new ReactorClientHttpConnector(createDefaultHttpClient()))
                .defaultHeader(HEADER_API_KEY, property.apiKey())
                .defaultHeader(HEADER_SERVICE_NAME, property.serviceName())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private HttpClient createDefaultHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT_MILLIS)
                .responseTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(TIMEOUT_SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(TIMEOUT_SECONDS))
                );
    }
}
