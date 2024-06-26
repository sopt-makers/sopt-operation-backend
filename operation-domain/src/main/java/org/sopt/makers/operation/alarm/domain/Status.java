package org.sopt.makers.operation.alarm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    SCHEDULED("발송 예약"),
    COMPLETED("발송 완료");

    private final String description;
}
