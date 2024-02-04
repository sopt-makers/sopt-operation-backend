package org.sopt.makers.operation.common.exception;

public class MemberException extends RuntimeException {
    public MemberException(String message) {
        super("[MemberException] : " + message);
    }
}
