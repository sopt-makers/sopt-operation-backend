package org.sopt.makers.operation.user.domain;

import java.util.Arrays;

public enum SocialType {
    GOOGLE,
    APPLE;


    public static boolean isContains(String type) {
        return Arrays.stream(SocialType.values())
                .anyMatch(socialType -> socialType.name().equals(type));
    }
}
