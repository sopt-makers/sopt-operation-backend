package org.sopt.makers.operation.user.domain;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;

@Entity @Getter
@Table(name = "user_identity_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserIdentityInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "idp_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @Column(name = "auth_user_id", nullable = false)
    private String socialId;

    @Builder
    public UserIdentityInfo(Long userId, SocialType socialType, String socialId) {
        this.userId = userId;
        this.socialType = socialType;
        this.socialId = socialId;
    }
}
