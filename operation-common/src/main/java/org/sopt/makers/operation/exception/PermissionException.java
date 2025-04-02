package org.sopt.makers.operation.exception;

import lombok.Getter;
import org.sopt.makers.operation.code.failure.FailureCode;

@Getter
public class PermissionException extends RuntimeException {

    private final FailureCode failureCode;

    public PermissionException(FailureCode failureCode) {
        super("[PermissionException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }

}
