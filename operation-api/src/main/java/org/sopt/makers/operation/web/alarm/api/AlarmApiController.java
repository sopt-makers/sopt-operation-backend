package org.sopt.makers.operation.web.alarm.api;

import static org.sopt.makers.operation.web.alarm.message.SuccessMessage.*;

import org.operation.alarm.domain.Status;
import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.common.dto.BaseResponse;
import org.sopt.makers.operation.common.util.ApiResponseUtil;
import org.sopt.makers.operation.common.util.CommonUtils;
import org.sopt.makers.operation.dto.AlarmRequest;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmSendRequest;
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

import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
public class AlarmApiController implements AlarmApi {

	private final AlarmService alarmService;
	private final CommonUtils utils;

	@Override
	@PostMapping("/send")
	public ResponseEntity<BaseResponse<?>> sendAlarm(AlarmSendRequest request) {
		alarmService.sendByAdmin(request);
		return ApiResponseUtil.ok(SUCCESS_SEND_ALARM.getContent());
	}

	@Override
	@PostMapping
	public ResponseEntity<BaseResponse<?>> createAlarm(AlarmRequest request) {
		val alarmId = alarmService.createAlarm(request);
		val uri = utils.getURI("/{alarmId}", alarmId);
		return ApiResponseUtil.created(uri, SUCCESS_CREATE_ALARM.getContent(), alarmId);
	}

	@Override
	@GetMapping
	public ResponseEntity<BaseResponse<?>> getAlarms(
			@RequestParam(required = false) Integer generation,
			@RequestParam(required = false) Part part,
			@RequestParam(required = false) Status status,
			Pageable pageable
	) {
		val response = alarmService.getAlarms(generation, part, status, pageable);
		return ApiResponseUtil.ok(SUCCESS_GET_ALARMS.getContent(), response);
	}

	@Override
	@GetMapping("/{alarmId}")
	public ResponseEntity<BaseResponse<?>> getAlarm(@PathVariable long alarmId) {
		val response = alarmService.getAlarm(alarmId);
		return ApiResponseUtil.ok(SUCCESS_GET_ALARM.getContent(), response);
	}

	@Override
	@DeleteMapping("/{alarmId}")
	public ResponseEntity<BaseResponse<?>> deleteAlarm(@PathVariable long alarmId) {
		alarmService.deleteAlarm(alarmId);
		return ApiResponseUtil.ok(SUCCESS_DELETE_ALARM.getContent());
	}
}
