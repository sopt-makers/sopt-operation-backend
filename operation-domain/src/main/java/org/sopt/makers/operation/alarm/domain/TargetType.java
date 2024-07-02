package org.sopt.makers.operation.alarm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TargetType {
    ALL("sendAll"),
    ACTIVE("send"),
    CSV("send");

    private final String action;
}
