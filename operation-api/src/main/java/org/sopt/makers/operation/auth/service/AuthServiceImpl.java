package org.sopt.makers.operation.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.auth.repository.TeamOAuthInfoRepository;
import org.sopt.makers.operation.client.social.SocialLoginManager;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.AuthException;
import org.sopt.makers.operation.user.domain.SocialType;
import org.sopt.makers.operation.user.repository.identityinfo.UserIdentityInfoRepository;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.time.ZoneId;
import java.util.Date;

import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.INVALID_SOCIAL_CODE;
import static org.sopt.makers.operation.code.failure.auth.AuthFailureCode.NOT_FOUND_USER_SOCIAL_IDENTITY_INFO;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final SocialLoginManager socialLoginManager;
    private final TeamOAuthInfoRepository teamOAuthInfoRepository;
    private final UserIdentityInfoRepository userIdentityInfoRepository;
    private final ValueConfig valueConfig;

    @Override
    public boolean checkRegisteredTeamOAuthInfo(String clientId, String redirectUri) {
        return teamOAuthInfoRepository.existsByClientIdAndRedirectUri(clientId, redirectUri);
    }

    @Override
    public String getSocialUserInfo(SocialType type, String code) {
        val idToken = socialLoginManager.getIdTokenByCode(type, code);
        if (idToken == null) throw new AuthException(INVALID_SOCIAL_CODE);
        return socialLoginManager.getUserInfo(idToken);
    }

    @Override
    public Long getUserId(SocialType socialType, String userSocialId) {
        val userIdentityInfo = userIdentityInfoRepository.findBySocialTypeAndSocialId(socialType, userSocialId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_USER_SOCIAL_IDENTITY_INFO));
        return userIdentityInfo.getUserId();
    }

    @Override
    public String generatePlatformCode(String clientId, String redirectUri, Long userId) {
        val platformCodeSecretKey = valueConfig.getPlatformCodeSecretKey();

        val signatureAlgorithm = SignatureAlgorithm.HS256;
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(platformCodeSecretKey);
        val signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
        val exp = new Date().toInstant().atZone(KST)
                .toLocalDateTime().plusMinutes(5).atZone(KST).toInstant();
        return Jwts.builder()
                .setIssuer(clientId)
                .setAudience(redirectUri)
                .setSubject(Long.toString(userId))
                .setExpiration(Date.from(exp))
                .signWith(signingKey, signatureAlgorithm)
                .compact();
    }
}
