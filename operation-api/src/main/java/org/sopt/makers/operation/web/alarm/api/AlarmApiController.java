package org.sopt.makers.operation.web.alarm.api;

import static org.sopt.makers.operation.code.success.web.AlarmSuccessCode.SUCCESS_DELETE_ALARM;
import static org.sopt.makers.operation.code.success.web.AlarmSuccessCode.SUCCESS_GET_ALARM;
import static org.sopt.makers.operation.code.success.web.AlarmSuccessCode.SUCCESS_GET_ALARMS;
import static org.sopt.makers.operation.code.success.web.AlarmSuccessCode.SUCCESS_SCHEDULE_ALARM;
import static org.sopt.makers.operation.code.success.web.AlarmSuccessCode.SUCCESS_SEND_ALARM;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.alarm.domain.Status;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmInstantSendRequest;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmScheduleSendRequest;
import org.sopt.makers.operation.web.alarm.service.AlarmService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
public class AlarmApiController implements AlarmApi {

    private final AlarmService alarmService;

    @Override
    @PostMapping("/send")
    public ResponseEntity<BaseResponse<?>> sendInstantAlarm(@Valid AlarmInstantSendRequest request) {
        alarmService.sendInstantAlarm(request);
        return ApiResponseUtil.success(SUCCESS_SEND_ALARM);
    }

    @Override
    @PostMapping("/schedule")
    public ResponseEntity<BaseResponse<?>> sendScheduleAlarm(@Valid AlarmScheduleSendRequest request) {
        alarmService.sendScheduleAlarm(request);
        return ApiResponseUtil.success(SUCCESS_SCHEDULE_ALARM);
    }

    @Override
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAlarms(
            @RequestParam(required = false) Integer generation,
            @RequestParam(required = false) Status status,
            Pageable pageable
    ) {
        val response = alarmService.getAlarms(generation, status, pageable);
        return ApiResponseUtil.success(SUCCESS_GET_ALARMS, response);
    }

    @Override
    @GetMapping("/{alarmId}")
    public ResponseEntity<BaseResponse<?>> getAlarm(@PathVariable long alarmId) {
        val response = alarmService.getAlarm(alarmId);
        return ApiResponseUtil.success(SUCCESS_GET_ALARM, response);
    }

    @Override
    @DeleteMapping("/{alarmId}")
    public ResponseEntity<BaseResponse<?>> deleteAlarm(@PathVariable long alarmId) {
        alarmService.deleteAlarm(alarmId);
        return ApiResponseUtil.success(SUCCESS_DELETE_ALARM);
    }
}
