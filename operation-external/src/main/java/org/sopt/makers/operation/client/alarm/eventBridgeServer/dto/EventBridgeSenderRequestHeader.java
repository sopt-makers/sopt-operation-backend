package org.sopt.makers.operation.client.alarm.eventBridgeServer.dto;

import java.util.UUID;
import lombok.val;

public record EventBridgeSenderRequestHeader(
        String action,
        String xApiKey,
        String transactionId,
        String service
) {
    public static EventBridgeSenderRequestHeader of(String xApiKey, String action, String service) {
        val transactionId = UUID.randomUUID().toString();
        return new EventBridgeSenderRequestHeader(action, xApiKey, transactionId, service);
    }
}
