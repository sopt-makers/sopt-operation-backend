package org.operation.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public record BaseResponse<T> (
	boolean success,
	String message,
	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	T data
) {

	public static <T> BaseResponse<?> success(String message, T data) {
		return BaseResponse.builder()
				.success(true)
				.message(message)
				.data(data)
				.build();
	}

	public static BaseResponse<?> success(String message) {
		return BaseResponse.builder()
				.success(true)
				.message(message)
				.build();
	}

	public static BaseResponse<?> fail(String message) {
		return BaseResponse.builder()
				.success(false)
				.message(message)
				.build();
	}
}
