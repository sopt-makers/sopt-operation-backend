package org.sopt.makers.operation.client.eventbridge.dto;

import java.util.List;
import org.sopt.makers.operation.alarm.domain.Category;
import org.sopt.makers.operation.config.ValueConfig;

public record EventBridgeSenderRequest(EventBridgeSenderRequestHeader header,
                                       EventBridgeSenderRequestBody body) {
    public static EventBridgeSenderRequest of(EventBridgeSenderRequestHeader header,
                                              EventBridgeSenderRequestBody body) {
        return new EventBridgeSenderRequest(header, body);
    }

    public static EventBridgeSenderRequest of(
            ValueConfig config, String action,
            List<String> userIds, String title, String content, Category category, String deepLink, String webLink
    ) {
        EventBridgeSenderRequestHeader Header = EventBridgeSenderRequestHeader.of(config, action);
        EventBridgeSenderRequestBody Body = EventBridgeSenderRequestBody.of(userIds, title, content, category, deepLink,
                webLink);
        return new EventBridgeSenderRequest(Header, Body);
    }
}
