package org.sopt.makers.operation.code.success;

import org.springframework.http.HttpStatus;

public interface SuccessCode {
	HttpStatus getStatus();
	String getMessage();
}
