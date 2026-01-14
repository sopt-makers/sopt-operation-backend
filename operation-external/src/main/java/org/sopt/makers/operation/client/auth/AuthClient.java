package org.sopt.makers.operation.client.auth;

import lombok.extern.slf4j.Slf4j;
import org.sopt.makers.operation.code.failure.ClientFailure;
import org.sopt.makers.operation.exception.ClientException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@Component
public class AuthClient {

    private static final String HEADER_API_KEY = "X-Api-Key";
    private static final String HEADER_SERVICE_NAME = "X-Service-Name";

    private final RestTemplate restTemplate;
    private final AuthClientProperty authProperty;

    public AuthClient(AuthClientProperty authProperty, RestTemplateBuilder restTemplateBuilder) {
        this.authProperty = authProperty;
        this.restTemplate = restTemplateBuilder
                .rootUri(authProperty.url())
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }

    public String getJwk() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HEADER_API_KEY, authProperty.apiKey());
            headers.set(HEADER_SERVICE_NAME, authProperty.serviceName());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            return restTemplate.exchange(
                    authProperty.endpoints().jwk(),
                    HttpMethod.GET,
                    entity,
                    String.class
            ).getBody();
        } catch (RestClientException e) {
            log.error("Failed to receive response from Auth server: {}", e.getMessage(), e);
            throw new ClientException(ClientFailure.RESPONSE_ERROR);
        } catch (RuntimeException e) {
            log.error("Unexpected exception occurred during Auth server communication: {}", e.getMessage(), e);
            throw new ClientException(ClientFailure.COMMUNICATION_ERROR);
        }
    }
}
