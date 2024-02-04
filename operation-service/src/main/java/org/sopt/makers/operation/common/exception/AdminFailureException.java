package org.sopt.makers.operation.common.exception;

public class AdminFailureException extends RuntimeException {
    public AdminFailureException(String message) {
        super("[AuthFailureException] : " + message);
    }
}
