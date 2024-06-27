package org.sopt.makers.operation.exception;

import lombok.Getter;
import org.sopt.makers.operation.code.failure.FailureCode;

@Getter
public class UserException extends RuntimeException{

    private final FailureCode failureCode;

    public UserException(FailureCode failureCode) {
        super("[UserException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }
}
