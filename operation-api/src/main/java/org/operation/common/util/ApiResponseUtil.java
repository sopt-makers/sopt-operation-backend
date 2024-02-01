package org.operation.common.util;

import org.operation.common.dto.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface ApiResponseUtil {

	static <T> ResponseEntity<BaseResponse<?>> ok(String message, T data) {
		return ResponseEntity.ok(BaseResponse.of(message, data));
	}

	static ResponseEntity<BaseResponse<?>> ok(String message) {
		return ResponseEntity.ok(BaseResponse.of(message, true));
	}
}
