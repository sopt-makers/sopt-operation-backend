package org.sopt.makers.operation.service;

import java.util.List;

import org.sopt.makers.operation.dto.alarm.AlarmSendRequestDTO;
import org.sopt.makers.operation.entity.alarm.Attribute;

public interface AlarmService {
    void sendAdmin(AlarmSendRequestDTO requestDTO);
    void send(String title, String content, List<String> targetList, Attribute attribute, String link);
}
