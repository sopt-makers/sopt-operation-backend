package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;

import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.dto.alarm.AlarmInactiveListResponseDTO;
import org.sopt.makers.operation.dto.alarm.AlarmSendRequestDTO;
import org.sopt.makers.operation.dto.alarm.AlarmSendResponseDTO;
import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.alarm.Attribute;
import org.sopt.makers.operation.exception.AlarmException;
import org.sopt.makers.operation.repository.AlarmRepository;
import org.sopt.makers.operation.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    @Value("${sopt.makers.playground.server}")
    private String playGroundURI;

    @Value("${sopt.makers.playground.token}")
    private String playGroundToken;

    @Value("${sopt.current.generation}")
    private int currentGeneration;
    private final RestTemplate restTemplate = new RestTemplate();

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    private final List<String> appLinkList = Arrays.asList(
            "home", "home/notification", "home/mypage", "home/attendance",
            "home/attendance/attendance-modal", "home/soptamp",
            "home/soptamp/entire-ranking", "home/soptamp/current-generation-ranking"
    );

    private final List<String> webLinkList = Arrays.asList(
            "https://playground.sopt.org/members",
            "https://playground.sopt.org/group"
    );

    @Override
    public void sendAdmin(AlarmSendRequestDTO requestDTO) {
        val alarm = alarmRepository.findById(requestDTO.alarmId())
                .orElseThrow(() -> new EntityNotFoundException(INVALID_ALARM.getName()));

        val targetList = alarm.getTargetList();

        List<String> targetIdList;
        if (targetList.size() != 0) {
            targetIdList = alarm.getTargetList();
        } else {
            if (alarm.isActive()) {
                targetIdList = extractCurrentTargetList(alarm.getPart());
            } else {
                targetIdList = extractInactiveTargetList(currentGeneration, alarm.getPart())
                        .memberIds().stream()
                        .map(String::valueOf).toList();
            }
        }

        send(alarm.getTitle(), alarm.getContent(), targetIdList, alarm.getAttribute(), alarm.getLink());
    }

    private List<String> extractCurrentTargetList(Part part) {
        val memberList = memberRepository.search(new MemberSearchCondition(part, currentGeneration));
        return memberList.stream()
                .map(member -> String.valueOf(member.getPlaygroundId()))
                .toList();
    }

    private AlarmInactiveListResponseDTO extractInactiveTargetList(int generation, Part part) {
        val getInactiveUserURL = playGroundURI + "/internal/api/v1/members/inactivity?generation=" + generation + "&part=" + part;

        val headers = new HttpHeaders();
        headers.add("content-type", "application/json;charset=UTF-8");
        headers.add("Authorization", playGroundToken);

        val entity = new HttpEntity<>(null, headers);

        try {
            val response = restTemplate.exchange(
                    getInactiveUserURL,
                    HttpMethod.GET,
                    entity,
                    AlarmInactiveListResponseDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new AlarmException(FAIL_INACTIVE_USERS.getName());
        }
    }

    public void send(String title, String content, List<String> targetList, Attribute attribute, String link) {
        val alarmRequest = new HashMap<>();
        
        alarmRequest.put("userIds", targetList);
        alarmRequest.put("title", title);
        alarmRequest.put("content", content);
        alarmRequest.put("category", attribute);

        if (Objects.nonNull(link)) {
            if (appLinkList.contains(link)) {
                alarmRequest.put("appLink", link);
            } else {
                alarmRequest.put("webLink", link);
            }
        }

        val headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("action","send");
        headers.add("transactionId",UUID.randomUUID().toString());
        headers.add("service","operation");
        headers.add("x-api-key", key);

        val entity = new HttpEntity<>(alarmRequest, headers);

        try {
            restTemplate.postForEntity(host, entity, AlarmSendResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw new AlarmException(FAIL_SEND_ALARM.getName());
        }
    }
}
