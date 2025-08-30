package org.sopt.makers.operation.exception;

import org.sopt.makers.operation.code.failure.FailureCode;

public abstract class BaseException extends RuntimeException {

    private final FailureCode failure;

    public BaseException(final FailureCode failure) {
        super(failure.getMessage());
        this.failure = failure;
    }

    public FailureCode getError() {
        return this.failure;
    }
}
