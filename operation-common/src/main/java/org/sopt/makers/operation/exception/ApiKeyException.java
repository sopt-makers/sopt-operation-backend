package org.sopt.makers.operation.exception;

import org.sopt.makers.operation.code.failure.FailureCode;
import lombok.Getter;

@Getter
public class ApiKeyException extends RuntimeException {
    private final FailureCode failureCode;

    public ApiKeyException(FailureCode failureCode) {
        super("[ApiKeyException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }
}