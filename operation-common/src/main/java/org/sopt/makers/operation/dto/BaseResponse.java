package org.sopt.makers.operation.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record BaseResponse<T> (
	boolean success,
	String message,
	@JsonInclude(value = NON_NULL)
	T data
) {

	public static <T> BaseResponse<?> of(String message, T data) {
		return BaseResponse.builder()
				.success(true)
				.message(message)
				.data(data)
				.build();
	}

	public static BaseResponse<?> of(boolean isSuccess, String message) {
		return BaseResponse.builder()
				.success(isSuccess)
				.message(message)
				.build();
	}
}
