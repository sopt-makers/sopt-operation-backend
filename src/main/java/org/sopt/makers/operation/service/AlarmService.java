package org.sopt.makers.operation.service;

import org.sopt.makers.operation.dto.alarm.AlarmSendRequestDTO;

public interface AlarmService {
    void sendAdmin(AlarmSendRequestDTO requestDTO);
}
