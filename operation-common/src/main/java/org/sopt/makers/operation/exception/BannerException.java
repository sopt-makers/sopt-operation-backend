package org.sopt.makers.operation.exception;

import lombok.Getter;
import org.sopt.makers.operation.code.failure.FailureCode;

@Getter
public class BannerException extends RuntimeException {
    private final FailureCode failureCode;

    public BannerException(FailureCode failureCode) {
        super("[BannerException] : " + failureCode.getMessage());
        this.failureCode = failureCode;
    }

}
