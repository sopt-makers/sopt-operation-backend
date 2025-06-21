package org.sopt.makers.operation.exception;


import org.sopt.makers.operation.code.failure.ClientFailure;

public class ClientException extends BaseException {
    public ClientException(ClientFailure failure) {
        super(failure);
    }
}
