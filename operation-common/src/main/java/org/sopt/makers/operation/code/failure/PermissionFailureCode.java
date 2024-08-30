package org.sopt.makers.operation.code.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PermissionFailureCode implements FailureCode {
    // 403
    NO_PERMISSION_ONLY_SELF(HttpStatus.FORBIDDEN, "해당 API는 본인만 요청할 수 있습니다."),
    NO_PERMISSION_ONLY_SELF_INCLUDE_EXECUTIVE(HttpStatus.FORBIDDEN, "해당 API는 본인 포함 임원진까지만 요청할 수 있습니다."),

    ;
    private final HttpStatus status;
    private final String message;

}
