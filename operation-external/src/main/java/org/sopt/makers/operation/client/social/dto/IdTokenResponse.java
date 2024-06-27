package org.sopt.makers.operation.client.social.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IdTokenResponse(
        @JsonProperty("id_token")
        String idToken
) {
}
