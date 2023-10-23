package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;

import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.dto.alarm.AlarmSendRequestDTO;
import org.sopt.makers.operation.dto.alarm.AlarmSendResponseDTO;
import org.sopt.makers.operation.exception.AlarmException;
import org.sopt.makers.operation.repository.AlarmRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import lombok.val;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    @Value("${notification.key}")
    private String key;

    @Value("${notification.url}")
    private String host;
    private final RestTemplate notificationClient = new RestTemplate();

    private final AlarmRepository alarmRepository;

    @Override
    public void send(AlarmSendRequestDTO requestDTO) {
        val alarm = alarmRepository.findById(requestDTO.alarmId())
                .orElseThrow(() -> new EntityNotFoundException(INVALID_ALARM.getName()));

        val alarmRequest = new HashMap<>();
        //TODO: null이면 파트와 활동기수여부로 아이디 추출
        alarmRequest.put("userIds", alarm.getTargetList());

        alarmRequest.put("title", alarm.getTitle());
        alarmRequest.put("content", alarm.getContent());
        alarmRequest.put("category", alarm.getAttribute());

        if (Objects.nonNull(alarm.getLink())) {
            //TODO: 웹/앱 링크 구분 메소드 생성
            alarmRequest.put("webLink", alarm.getLink());
        }

        val headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("action","send");
        headers.add("transactionId","ewasfsafsaf");
        headers.add("service","operation");
        headers.add("x-api-key", key);

        val entity = new HttpEntity<>(alarmRequest, headers);

        try {
            notificationClient.postForEntity(host, entity, AlarmSendResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw new AlarmException(FAIL_SEND_ALARM.getName());
        }
    }
}
