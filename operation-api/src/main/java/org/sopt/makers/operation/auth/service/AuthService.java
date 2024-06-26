package org.sopt.makers.operation.auth.service;

import org.sopt.makers.operation.user.domain.SocialType;

public interface AuthService {
    boolean checkRegisteredTeamOAuthInfo(String clientId, String redirectUri);

    String getSocialUserInfo(SocialType type, String code);

    Long getUserId(SocialType socialType, String userSocialId);
}
