package org.sopt.makers.operation.client.alarm.eventBridgeServer.dto;

import java.util.UUID;

public record EventBridgeSenderRequestHeader(String action,
                                             String xApiKey,
                                             String transactionId,
                                             String service) {
    public static EventBridgeSenderRequestHeader of(String xApiKey, String action) {
        String transactionId = UUID.randomUUID().toString();
        return new EventBridgeSenderRequestHeader(action, xApiKey, transactionId, "operation");
    }
}
