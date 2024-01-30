package org.sopt.makers.operation.dto.alarm.response;

public record AlarmSendResponseDTO(
        int status,
        boolean success,
        String message
) {
}