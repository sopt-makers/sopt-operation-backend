package org.sopt.makers.operation.client.social;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.client.social.dto.IdTokenResponse;
import org.sopt.makers.operation.exception.AuthException;
import org.sopt.makers.operation.user.domain.SocialType;
import org.springframework.stereotype.Component;

import java.text.ParseException;

import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.INVALID_ID_TOKEN;

@Component
@RequiredArgsConstructor
public class SocialLoginManager {
    private final AppleSocialLogin appleSocialLogin;
    private final GoogleSocialLogin googleSocialLogin;

    public IdTokenResponse getIdTokenByCode(SocialType type, String code) {
        return switch (type) {
            case APPLE -> appleSocialLogin.getIdTokenByCode(code);
            case GOOGLE -> googleSocialLogin.getIdTokenByCode(code);
        };
    }

    public String getUserInfo(IdTokenResponse tokenResponse) {
        val idToken = tokenResponse.idToken();
        try {
            val signedJWT = SignedJWT.parse(idToken);
            val payload = signedJWT.getJWTClaimsSet();
            val userId = payload.getSubject();
            return userId;
        } catch (ParseException e) {
            throw new AuthException(INVALID_ID_TOKEN);
        }
    }
}
