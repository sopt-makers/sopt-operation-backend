package org.sopt.makers.operation.dto;

public record ResponseDTO(
	boolean success,
	String message,
	Object data
) {

	public static ResponseDTO success(String message, Object data) {
		return new ResponseDTO(true, message, data);
	}

	public static ResponseDTO success(String message) {
		return new ResponseDTO(true, message, null);
	}

	public static ResponseDTO fail(String message) {
		return new ResponseDTO(false, message, null);
	}
}
