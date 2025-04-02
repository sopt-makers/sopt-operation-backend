package org.sopt.makers.operation.alarm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmStatus {
    SCHEDULED("예약 완료"),
    COMPLETED("발송 완료"),
    FAILED("발송 실패");

    private final String description;
}
