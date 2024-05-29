package org.sopt.makers.operation.user.domain;

import java.util.Arrays;

public enum SocialType {
    GOOGLE,
    APPLE;


    public static boolean isContains(String type) {
        SocialType[] socialTypes = SocialType.values();
        return Arrays.stream(socialTypes)
                .anyMatch(socialType -> socialType.name().equals(type));
    }
}
