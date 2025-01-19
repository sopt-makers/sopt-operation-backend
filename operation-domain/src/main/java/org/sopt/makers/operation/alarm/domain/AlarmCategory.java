package org.sopt.makers.operation.alarm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmCategory {
    NOTICE("공지"),
    NEWS("소식");

    private final String name;
}
