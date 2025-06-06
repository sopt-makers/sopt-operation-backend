package org.sopt.makers.operation.client.alarm;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.alarm.domain.AlarmLinkType;
import org.sopt.makers.operation.alarm.domain.AlarmSendAction;
import org.sopt.makers.operation.alarm.domain.AlarmTargetType;
import org.sopt.makers.operation.client.alarm.dto.AlarmRequest;
import org.sopt.makers.operation.client.alarm.dto.InstantAlarmRequest;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.AlarmException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.UUID.randomUUID;
import static org.sopt.makers.operation.code.failure.AlarmFailureCode.FAIL_SEND_ALARM;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
class InstantAlarmSender implements AlarmSender{
    private final RestTemplate restTemplate;
    private final ValueConfig valueConfig;

    @Override
    public void sendAlarm(AlarmRequest alarmRequest) {
        val instantRequest = (InstantAlarmRequest) alarmRequest;
        try {
            val host = valueConfig.getNOTIFICATION_URL();
            val body = generateBody(instantRequest);
            val headers = generateHeader(instantRequest);
            val request = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(host, request, InstantAlarmRequest.class);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw new AlarmException(FAIL_SEND_ALARM);
        }
    }

    private Map<Object, Object> generateBody(InstantAlarmRequest instantRequest) {
        val body = new HashMap<>();
        putRequiredAttributes(instantRequest, body);
        putOptionalAttributes(instantRequest, body);
        return body;
    }

    private static void putRequiredAttributes(InstantAlarmRequest instantRequest, HashMap<Object, Object> body) {
        body.put("title", instantRequest.title());
        body.put("content", instantRequest.content());
        body.put("category", instantRequest.category());
    }

    private static void putOptionalAttributes(InstantAlarmRequest instantRequest, HashMap<Object, Object> body) {
        val isTargetAll = Objects.equals(instantRequest.targetType(), AlarmTargetType.ALL);
        val isWebLink = Objects.equals(instantRequest.linkType(), AlarmLinkType.WEB);
        val isAppLink = Objects.equals(instantRequest.linkType(), AlarmLinkType.APP);

        if (!isTargetAll) {
            body.put("userIds", instantRequest.targets());
        }
        if (isWebLink) {
            body.put("webLink", instantRequest.link());
        } else if (isAppLink) {
            body.put("deepLink", instantRequest.link());
        }
    }

    private HttpHeaders generateHeader(InstantAlarmRequest instantRequest) {
        val headers = new HttpHeaders();
        val apiKey = valueConfig.getNOTIFICATION_KEY();
        val actionValue = instantRequest.targetType() != null && instantRequest.targetType().getAction() != null
                ? instantRequest.targetType().getAction().getValue()
                : AlarmSendAction.SEND.getValue();

        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.setAccept(Collections.singletonList(APPLICATION_JSON));

        headers.add("action", actionValue);
        headers.add("transactionId", randomUUID().toString());
        headers.add("service", "operation");
        headers.add("x-api-key", apiKey);
        return headers;
    }

}
