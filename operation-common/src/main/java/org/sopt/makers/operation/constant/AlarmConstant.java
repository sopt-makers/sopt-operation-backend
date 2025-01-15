package org.sopt.makers.operation.constant;

import lombok.RequiredArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public final class AlarmConstant {
    private static final List<String> SUPPORTED_APP_LINK = List.of(
            "home",
            "home/notification",
            "home/mypage",
            "home/attendance",
            "home/attendance/attendance-modal",
            "home/soptamp",
            "home/soptamp/entire-ranking",
            "home/soptamp/current-generation-ranking"
    );

    public static boolean isSupportedAppLink(String link) {
        return SUPPORTED_APP_LINK.contains(link);
    }

}
