package org.sopt.makers.operation.auth.repository;

import org.sopt.makers.operation.auth.domain.TeamOAuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamOAuthInfoRepository extends JpaRepository<TeamOAuthInfo, Long> {
    boolean existsByClientIdAndRedirectUri(String clientId, String redirectUri);
}
