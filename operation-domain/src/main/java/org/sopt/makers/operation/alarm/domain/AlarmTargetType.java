package org.sopt.makers.operation.alarm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmTargetType {
    ALL("전체 회원", AlarmSendAction.SEND_ALL),
    ACTIVE("활동 회원", AlarmSendAction.SEND),
    CSV("CSV", AlarmSendAction.SEND),
    ;

    private final String name;
    private final AlarmSendAction action;
}
