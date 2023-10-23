package org.sopt.makers.operation.dto.alarm;

public record AlarmSendResponseDTO(
        int status,
        boolean success,
        String message
) {
}