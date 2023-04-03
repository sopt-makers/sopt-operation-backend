package org.sopt.makers.operation.common;

public record ApiResponse(
	boolean success,
	String message,
	Object data
) {

	public static ApiResponse success(String message, Object data) {
		return new ApiResponse(true, message, data);
	}

	public static ApiResponse success(String message) {
		return new ApiResponse(true, message, null);
	}

	public static ApiResponse fail(String message) {
		return new ApiResponse(false, message, null);
	}
}
