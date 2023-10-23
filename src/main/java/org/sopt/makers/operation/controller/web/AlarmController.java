package org.sopt.makers.operation.controller.web;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.alarm.AlarmSendRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.service.AlarmService;
import org.sopt.makers.operation.service.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.sopt.makers.operation.common.ResponseMessage.SUCCESS_SEND_ALARM;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
public class AlarmController {
    private final AlarmService alarmService;

    @ApiOperation(value = "알림 전송")
    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendAlarm(@RequestBody AlarmSendRequestDTO requestDTO) {
        alarmService.send(requestDTO);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_SEND_ALARM.getMessage()));
    }
}
