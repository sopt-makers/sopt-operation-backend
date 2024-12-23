package org.sopt.makers.operation.code.failure;

import static org.springframework.http.HttpStatus.*;

import lombok.*;
import org.springframework.http.*;

@RequiredArgsConstructor
@Getter
public enum ExternalFailureCode implements FailureCode {
    FAIL_FOUND_S3_RESOURCE(NOT_FOUND, "S3에서 객체를 찾지 못했습니다");

    private final HttpStatus status;
    private final String message;
}
