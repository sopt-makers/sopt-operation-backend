package org.sopt.makers.operation.exception;

import org.sopt.makers.operation.code.failure.FailureCode;

import lombok.Getter;

@Getter
public class AttendanceException extends RuntimeException {

    private final FailureCode failureCode;

    public AttendanceException(FailureCode failureCode) {
        super("[AttendanceException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }
}
