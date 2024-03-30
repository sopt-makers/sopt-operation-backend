package org.sopt.makers.operation.exception;

import org.sopt.makers.operation.code.failure.FailureCode;

import lombok.Getter;

@Getter
public class TokenException extends RuntimeException {

    private final FailureCode failureCode;

    public TokenException(FailureCode failureCode) {
        super("[TokenException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }
}
