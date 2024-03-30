package org.sopt.makers.operation.exception;

import org.sopt.makers.operation.code.failure.FailureCode;

import lombok.Getter;

@Getter
public class AdminFailureException extends RuntimeException {

    private final FailureCode failureCode;

    public AdminFailureException(FailureCode failureCode) {
        super("[AuthFailureException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }
}
