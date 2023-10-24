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
import java.util.*;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    @Value("${notification.key}")
    private String key;

    @Value("${notification.url}")
    private String host;
    private final RestTemplate notificationClient = new RestTemplate();

    private final AlarmRepository alarmRepository;

    private final List<String> appLinkList = Arrays.asList(
            "home", "home/notification", "home/mypage", "home/attendance",
            "home/attendance/attendance-modal", "home/soptamp",
            "home/soptamp/entire-ranking", "home/soptamp/current-generation-ranking"
    );

    private final List<String> webLinkList = Arrays.asList();

    @Override
    public void send(AlarmSendRequestDTO requestDTO) {
        val alarm = alarmRepository.findById(requestDTO.alarmId())
                .orElseThrow(() -> new EntityNotFoundException(INVALID_ALARM.getName()));

        val targetList = alarm.getTargetList();
        val link = alarm.getLink();

        val alarmRequest = new HashMap<>();
        //TODO: null이면 파트와 활동기수여부로 아이디 추출
        if (Objects.nonNull(targetList)) {
            alarmRequest.put("userIds", targetList);
        } else {

        }

        alarmRequest.put("title", alarm.getTitle());
        alarmRequest.put("content", alarm.getContent());
        alarmRequest.put("category", alarm.getAttribute());

        if (Objects.nonNull(link)) {
            if (appLinkList.contains(link)) {
                alarmRequest.put("appLink", link);
            } else if (webLinkList.contains(link)) {
                alarmRequest.put("webLink", link);
            } else {
                throw new AlarmException(INVALID_LINK.getName());
            }
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
