package org.sopt.makers.operation.exception;

import org.sopt.makers.operation.code.failure.auth.JwtFailure;

public class JwtException extends BaseException {
    public JwtException(JwtFailure failure) {
        super(failure);
    }
}
