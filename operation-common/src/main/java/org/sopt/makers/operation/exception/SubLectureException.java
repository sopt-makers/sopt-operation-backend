package org.sopt.makers.operation.exception;

import org.sopt.makers.operation.code.failure.FailureCode;

import lombok.Getter;

@Getter
public class SubLectureException extends RuntimeException {

    private final FailureCode failureCode;

    public SubLectureException(FailureCode failureCode) {
        super("[SubLectureException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }
}
