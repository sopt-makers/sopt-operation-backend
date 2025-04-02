package org.sopt.makers.operation.exception;

import lombok.Getter;
import org.sopt.makers.operation.code.failure.FailureCode;


@Getter
public class ParameterDecodeCustomException extends RuntimeException {

    private final FailureCode failureCode;

    public ParameterDecodeCustomException(FailureCode failureCode, String parameter) {
        super(String.format("[ParameterDecodeCustomException] : %s (parameter = %s)", failureCode.getMessage(), parameter));
        this.failureCode = failureCode;
    }
}
