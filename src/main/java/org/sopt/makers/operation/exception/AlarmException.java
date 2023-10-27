package org.sopt.makers.operation.exception;

public class AlarmException extends RuntimeException {
    public AlarmException(String message) {
        super("[AlarmException] : " + message);
    }
}
