package org.sopt.makers.operation.user.repository.identityinfo;

import org.sopt.makers.operation.user.domain.SocialType;
import org.sopt.makers.operation.user.domain.UserIdentityInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserIdentityInfoRepository extends JpaRepository<UserIdentityInfo, Long> {

    Optional<UserIdentityInfo> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

}
