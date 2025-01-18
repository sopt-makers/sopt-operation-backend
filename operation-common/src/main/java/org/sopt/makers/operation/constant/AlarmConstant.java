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

    public static final String ALARM_REQUEST_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ALARM_REQUEST_TIME_FORMAT = "HH:mm";
    public static final String ALARM_RESPONSE_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ALARM_RESPONSE_TIME_FORMAT = "HH:mm";

    public static boolean isSupportedAppLink(String link) {
        return SUPPORTED_APP_LINK.contains(link);
    }

}
