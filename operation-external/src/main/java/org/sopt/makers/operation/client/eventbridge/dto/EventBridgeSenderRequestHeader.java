package org.sopt.makers.operation.client.eventbridge.dto;

import java.util.UUID;
import org.sopt.makers.operation.config.ValueConfig;

public record EventBridgeSenderRequestHeader(String action,
                                             String xApiKey,
                                             String transactionId,
                                             String service) {
    public static EventBridgeSenderRequestHeader of(ValueConfig config, String action) {
        String xApiKey = config.getNOTIFICATION_KEY();
        String transactionId = UUID.randomUUID().toString();
        return new EventBridgeSenderRequestHeader(action, xApiKey, transactionId, "operation");
    }
}
