package org.sopt.makers.operation.exception;

import org.sopt.makers.operation.code.failure.auth.JwkFailure;

public class JwkException extends BaseException {
    public JwkException(JwkFailure failure) {
        super(failure);
    }
}