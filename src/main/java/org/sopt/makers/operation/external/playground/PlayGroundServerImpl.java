package org.sopt.makers.operation.external.playground;

import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.sopt.makers.operation.entity.Part.*;
import static org.springframework.http.HttpMethod.*;

import org.sopt.makers.operation.dto.alarm.response.AlarmInactiveListResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.exception.AlarmException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Component
@RequiredArgsConstructor
public class PlayGroundServerImpl implements PlayGroundServer {
	private final RestTemplate restTemplate;

	@Value("${sopt.makers.playground.server}")
	private String playGroundURI;
	@Value("${sopt.makers.playground.token}")
	private String playGroundToken;

	@Override
	public AlarmInactiveListResponseDTO getInactiveMembers(int generation, Part part) {
		val uri = getInactiveUserURI(part, generation);
		val headers = getHeaders();
		val entity = new HttpEntity<>(null, headers);

		try {
			val response = restTemplate.exchange(uri, GET, entity, AlarmInactiveListResponseDTO.class);
			return response.getBody();
		} catch (Exception e) {
			throw new AlarmException(FAIL_INACTIVE_USERS.getName());
		}
	}

	private String getInactiveUserURI(Part part, int generation) {
		val uri = new StringBuilder();

		uri.append(playGroundURI)
			.append("/internal/api/v1/members/inactivity?generation=")
			.append(generation);

		if (!part.equals(ALL)) {
			uri.append("&part=").append(part);
		}

		return uri.toString();
	}

	private HttpHeaders getHeaders() {
		val headers = new HttpHeaders();
		headers.add("content-type", "application/json;charset=UTF-8");
		headers.add("Authorization", playGroundToken);
		return headers;
	}
}
