package org.sopt.makers.operation.client.alarm.eventBridgeServer.dto;

import java.util.List;
import org.sopt.makers.operation.alarm.domain.Category;

public record EventBridgeSenderRequestBody(
        List<String> userIds,
        String title,
        String content,
        Category category,
        String deepLink,
        String webLink
) {
    public static EventBridgeSenderRequestBody of(List<String> userIds, String title, String content, Category category,
                                                  String deepLink, String webLink) {
        return new EventBridgeSenderRequestBody(userIds, title, content, category, deepLink, webLink);
    }
}
