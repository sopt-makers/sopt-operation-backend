package org.sopt.makers.operation.constant;

import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public final class AlarmConstant {
    public static final String ALARM_REQUEST_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ALARM_REQUEST_TIME_FORMAT = "HH:mm";
    public static final String ALARM_REQUEST_SCHEDULE_TIME_FORMAT = "HH-mm";
    public static final String ALARM_RESPONSE_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ALARM_RESPONSE_TIME_FORMAT = "HH:mm";

}
