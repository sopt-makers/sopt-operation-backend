package org.sopt.makers.operation.exception;

import org.sopt.makers.operation.code.failure.FailureCode;

import lombok.Getter;

@Getter
public class ScheduleException extends RuntimeException {

    private final FailureCode failureCode;

    public ScheduleException(FailureCode failureCode) {
        super("[ScheduleException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }
}
