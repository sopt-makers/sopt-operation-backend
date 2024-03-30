package org.sopt.makers.operation.exception;

import org.sopt.makers.operation.code.failure.AlarmFailureCode;
import org.sopt.makers.operation.code.failure.FailureCode;

import lombok.Getter;

@Getter
public class AlarmException extends RuntimeException {

    private final FailureCode failureCode;

    public AlarmException(AlarmFailureCode failureCode) {
        super("[AlarmException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }
}
