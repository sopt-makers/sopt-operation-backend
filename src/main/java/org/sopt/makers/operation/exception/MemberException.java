package org.sopt.makers.operation.exception;

public class MemberException extends RuntimeException {
    public MemberException(String message) {
        super("[MemberException] : " + message);
    }
}
