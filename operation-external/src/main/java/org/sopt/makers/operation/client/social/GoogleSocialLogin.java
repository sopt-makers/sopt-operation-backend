package org.sopt.makers.operation.client.social;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.client.social.dto.IdTokenResponse;
import org.sopt.makers.operation.config.ValueConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class GoogleSocialLogin {
    private final RestTemplate restTemplate;
    private final ValueConfig valueConfig;

    public IdTokenResponse getIdTokenByCode(String code) {
        val params = new LinkedMultiValueMap<>();
        val grantType = "authorization_code";
        val clientId = valueConfig.getGoogleClientId();
        val clientSecret = valueConfig.getGoogleClientSecret();
        val redirectUri = valueConfig.getGoogleRedirectUrl();

        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("grant_type", grantType);
        params.add("redirect_uri", redirectUri);

        val headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        val entity = new HttpEntity<>(params, headers);

        val host = "https://oauth2.googleapis.com/token";
        return restTemplate.postForObject(host, entity, IdTokenResponse.class);
    }
}
