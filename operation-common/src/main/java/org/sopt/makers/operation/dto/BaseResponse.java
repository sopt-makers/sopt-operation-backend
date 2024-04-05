package org.sopt.makers.operation.dto;



import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record BaseResponse<T> (
	boolean success,
	String message,
//	@JsonInclude(value = NON_NULL)
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
