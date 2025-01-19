package org.sopt.makers.operation.alarm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmLinkType {
    WEB("웹"), APP("앱"), NONE("없음")
    ;
    private final String name;
}
