package org.sopt.makers.operation.exception;

import lombok.Getter;
import org.sopt.makers.operation.code.failure.FailureCode;

@Getter
public class AuthException extends RuntimeException{
    private final FailureCode failureCode;

    public AuthException(FailureCode failureCode) {
        super("[AuthException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }
}
