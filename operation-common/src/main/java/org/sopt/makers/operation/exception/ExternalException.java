package org.sopt.makers.operation.exception;

import lombok.*;
import org.sopt.makers.operation.code.failure.*;

@Getter
public class ExternalException extends RuntimeException {
    private final FailureCode failureCode;

    public ExternalException(FailureCode failureCode) {
        super("[ExternalException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }

}
