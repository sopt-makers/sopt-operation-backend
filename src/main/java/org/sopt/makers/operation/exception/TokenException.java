package org.sopt.makers.operation.exception;
public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super("[TokenException] : " + message);
    }
}
