package org.sopt.makers.operation.exception;

import org.sopt.makers.operation.code.failure.FailureCode;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {

    private final FailureCode failureCode;

    public MemberException(FailureCode failureCode) {
        super("[MemberException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }
}
