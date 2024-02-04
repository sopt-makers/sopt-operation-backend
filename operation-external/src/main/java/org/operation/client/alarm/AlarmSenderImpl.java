package org.operation.client.alarm;

import static java.util.Objects.*;
import static java.util.UUID.*;
import static org.operation.alarm.message.ErrorMessage.*;
import static org.springframework.http.MediaType.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.operation.common.config.ValueConfig;
import org.operation.common.exception.AlarmException;
import org.operation.web.alarm.dto.request.AlarmSenderRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Component
@RequiredArgsConstructor
public class AlarmSenderImpl implements AlarmSender {

	private final RestTemplate restTemplate;
	private final ValueConfig valueConfig;

	@Override
	public void send(AlarmSenderRequest request) {
		try {
			val host = valueConfig.getNOTIFICATION_URL();
			val entity = getEntity(request);
			restTemplate.postForEntity(host, entity, AlarmSenderRequest.class);
		} catch (HttpClientErrorException e) {
			throw new AlarmException(FAIL_SEND_ALARM.getContent());
		}
	}

	private HttpEntity<Map<Object, Object>> getEntity(AlarmSenderRequest request) {
		val alarmRequest = getAlarmRequest(request);
		val headers = getHeaders();
		return new HttpEntity<>(alarmRequest, headers);
	}

	private Map<Object, Object> getAlarmRequest(AlarmSenderRequest request) {
		val alarmRequest = new HashMap<>();
		val link = request.link();
		val linkKey = getLinkKey(link);

		if (!linkKey.isEmpty()) {
			alarmRequest.put(linkKey, link);
		}

		alarmRequest.put("userIds", request.targetList());
		alarmRequest.put("title", request.title());
		alarmRequest.put("content", request.content());
		alarmRequest.put("category", request.attribute());

		return alarmRequest;
	}

	private String getLinkKey(String link) {
		val appLinkList = valueConfig.getAPP_LINK_LIST();
		val webLinkList = valueConfig.getWEB_LINK_LIST();

		if (nonNull(link)) {
			if (appLinkList.contains(link)) {
				return "appLink";
			} else if (webLinkList.contains(link)) {
				return "webLink";
			}
		}
		return "";
	}

	private HttpHeaders getHeaders() {
		val headers = new HttpHeaders();
		val key = valueConfig.getNOTIFICATION_KEY();

		headers.setContentType(APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(APPLICATION_JSON));
		headers.add("action", "send");
		headers.add("transactionId", randomUUID().toString());
		headers.add("service", "operation");
		headers.add("x-api-key", key);

		return headers;
	}
}
