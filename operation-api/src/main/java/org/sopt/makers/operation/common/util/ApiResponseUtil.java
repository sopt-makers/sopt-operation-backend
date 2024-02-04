package org.sopt.makers.operation.common.util;

import org.sopt.makers.operation.code.failure.FailureCode;
import org.sopt.makers.operation.code.success.SuccessCode;
import org.sopt.makers.operation.common.dto.BaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface ApiResponseUtil {

	static <T> ResponseEntity<BaseResponse<?>> success(SuccessCode code, T data) {
		return ResponseEntity
				.status(code.getStatus())
				.body(BaseResponse.of(code.getMessage(), data));
	}

	static ResponseEntity<BaseResponse<?>> success(SuccessCode code) {
		return ResponseEntity
				.status(code.getStatus())
				.body(BaseResponse.of(true, code.getMessage()));
	}

	static <T> ResponseEntity<BaseResponse<?>> success(SuccessCode code, HttpHeaders headers, T data) {
		return ResponseEntity
				.status(code.getStatus())
				.headers(headers)
				.body(BaseResponse.of(code.getMessage(), data));
	}

	static <T> ResponseEntity<BaseResponse<?>> failure(FailureCode code) {
		return ResponseEntity
				.status(code.getStatus())
				.body(BaseResponse.of(false, code.getMessage()));
	}
}
