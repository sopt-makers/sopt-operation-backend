package org.sopt.makers.operation.client.alarm.eventBridgeServer.dto;

import java.util.List;
import org.sopt.makers.operation.alarm.domain.Category;

public record EventBridgeSenderRequest(
        EventBridgeSenderRequestHeader header,
        EventBridgeSenderRequestBody body
) {
    public static EventBridgeSenderRequest of(
            EventBridgeSenderRequestHeader header,
            EventBridgeSenderRequestBody body
    ) {
        return new EventBridgeSenderRequest(header, body);
    }

    public static EventBridgeSenderRequest of(
            String xApiKey, String service, String action,
            List<String> userIds, String title, String content, Category category, String deepLink, String webLink
    ) {
        EventBridgeSenderRequestHeader header = EventBridgeSenderRequestHeader.of(xApiKey, action, service);
        EventBridgeSenderRequestBody body = EventBridgeSenderRequestBody.of(userIds, title, content, category, deepLink,
                webLink);
        return new EventBridgeSenderRequest(header, body);
    }
}
