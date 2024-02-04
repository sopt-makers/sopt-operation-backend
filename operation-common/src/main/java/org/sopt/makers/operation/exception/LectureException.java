package org.sopt.makers.operation.exception;

import org.sopt.makers.operation.code.failure.FailureCode;

import lombok.Getter;

@Getter
public class LectureException extends RuntimeException {

    private final FailureCode failureCode;

    public LectureException(FailureCode failureCode) {
        super("[LectureException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }
}