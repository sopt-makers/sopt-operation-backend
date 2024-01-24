package org.sopt.makers.operation.external.alarm;

import static java.util.Objects.*;
import static java.util.UUID.*;
import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.springframework.http.MediaType.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopt.makers.operation.dto.alarm.AlarmSendResponseDTO;
import org.sopt.makers.operation.dto.alarm.AlarmSenderDTO;
import org.sopt.makers.operation.exception.AlarmException;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${notification.key}")
	private String key;
	@Value("${notification.url}")
	private String host;

	private final List<String> appLinkList = Arrays.asList(
		"home",
		"home/notification",
		"home/mypage",
		"home/attendance",
		"home/attendance/attendance-modal",
		"home/soptamp",
		"home/soptamp/entire-ranking",
		"home/soptamp/current-generation-ranking"
	);
	private final List<String> webLinkList = Arrays.asList(
		"https://playground.sopt.org/members",
		"https://playground.sopt.org/group"
	);

	@Override
	public void send(AlarmSenderDTO alarmSenderDTO) {
		val alarmRequest = getAlarmRequest(alarmSenderDTO);
		val headers = getHeaders();
		val entity = new HttpEntity<>(alarmRequest, headers);

		try {
			restTemplate.postForEntity(host, entity, AlarmSendResponseDTO.class);
		} catch (HttpClientErrorException e) {
			throw new AlarmException(FAIL_SEND_ALARM.getName());
		}
	}

	private Map<Object, Object> getAlarmRequest(AlarmSenderDTO alarmSenderDTO) {
		val alarmRequest = new HashMap<>();
		val link = alarmSenderDTO.link();
		val linkKey = getLinkKey(link);

		if (!linkKey.isEmpty()) {
			alarmRequest.put(linkKey, link);
		}

		alarmRequest.put("userIds", alarmSenderDTO.targetList());
		alarmRequest.put("title", alarmSenderDTO.title());
		alarmRequest.put("content", alarmSenderDTO.content());
		alarmRequest.put("category", alarmSenderDTO.attribute());

		return alarmRequest;
	}

	private String getLinkKey(String link) {
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

		headers.setContentType(APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(APPLICATION_JSON));
		headers.add("action", "send");
		headers.add("transactionId", randomUUID().toString());
		headers.add("service", "operation");
		headers.add("x-api-key", key);

		return headers;
	}
}
