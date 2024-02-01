package org.operation.common.util;

import org.operation.common.dto.BaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ApiResponseUtil {

	static <T> ResponseEntity<BaseResponse<?>> ok(String message, T data) {
		return ResponseEntity.ok(BaseResponse.of(message, data));
	}

	static ResponseEntity<BaseResponse<?>> ok(String message) {
		return ResponseEntity.ok(BaseResponse.of(message, true));
	}

	static <T> ResponseEntity<BaseResponse<?>> ok(HttpHeaders headers, String message, T data) {
		return ResponseEntity.status(HttpStatus.OK)
				.headers(headers)
				.body(BaseResponse.of(message, data));
	}

	static <T> ResponseEntity<BaseResponse<?>> failure(HttpStatus status, String message) {
		return ResponseEntity.status(status)
				.body(BaseResponse.of(message, false));
	}
}
