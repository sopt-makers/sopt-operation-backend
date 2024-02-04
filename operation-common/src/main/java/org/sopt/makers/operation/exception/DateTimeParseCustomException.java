package org.sopt.makers.operation.exception;

import java.time.format.DateTimeParseException;

import org.sopt.makers.operation.code.failure.FailureCode;

import lombok.Getter;

@Getter
public class DateTimeParseCustomException extends DateTimeParseException {

    private final FailureCode failureCode;

    public DateTimeParseCustomException(FailureCode failureCode, String input, int index) {
        super("[DateTimeParseException] : " + failureCode.getMessage(), input, index);
        this.failureCode = failureCode;
    }
}
