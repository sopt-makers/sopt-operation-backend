package org.sopt.makers.operation.client.playground;

import static org.sopt.makers.operation.code.failure.AlarmFailureCode.*;
import static org.sopt.makers.operation.member.domain.Part.*;
import static org.springframework.http.HttpMethod.*;

import org.sopt.makers.operation.client.playground.dto.MemberListGetResponse;
import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.AlarmException;
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
	private final ValueConfig valueConfig;

	@Override
	public MemberListGetResponse getMembers(int generation, Part part) {
		val uri = getPlaygroundMemberUri(part, generation);
		val headers = getHeaders();
		val entity = new HttpEntity<>(null, headers);

		try {
			val response = restTemplate.exchange(uri, GET, entity, MemberListGetResponse.class);
			return response.getBody();
		} catch (Exception e) {
			throw new AlarmException(FAIL_INACTIVE_USERS);
		}
	}

	private String getPlaygroundMemberUri(Part part, int generation) {
		val uri = new StringBuilder();
		val playGroundUri = valueConfig.getPlayGroundURI();

		uri.append(playGroundUri)
			.append("/internal/api/v1/members/inactivity?generation=")
			.append(generation);

		if (!part.equals(ALL)) {
			uri.append("&part=").append(part);
		}

		return uri.toString();
	}

	private HttpHeaders getHeaders() {
		val headers = new HttpHeaders();
		val playGroundToken = valueConfig.getPlayGroundToken();

		headers.add("content-type", "application/json;charset=UTF-8");
		headers.add("Authorization", playGroundToken);

		return headers;
	}
}
