package org.sopt.makers.operation.controller.web;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.alarm.AlarmSendRequestDTO;
import org.sopt.makers.operation.service.AlarmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.sopt.makers.operation.common.ResponseMessage.SUCCESS_SEND_ALARM;
import static org.sopt.makers.operation.common.ApiResponse.*;
import static org.sopt.makers.operation.common.ResponseMessage.*;

import java.net.URI;

import org.sopt.makers.operation.dto.alarm.AlarmRequestDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.alarm.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
public class AlarmController {
    private final AlarmService alarmService;

    @ApiOperation(value = "알림 전송")
    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendAlarm(@RequestBody AlarmSendRequestDTO requestDTO) {
        alarmService.sendAdmin(requestDTO);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_SEND_ALARM.getMessage()));
    }

	@ApiOperation("알림 생성")
	@PostMapping
	public ResponseEntity<ApiResponse> createAlarm(@RequestBody AlarmRequestDTO requestDTO) {
		val alarmId = alarmService.createAlarm(requestDTO);
		return ResponseEntity
			.created(getURI(alarmId))
			.body(success(SUCCESS_CREATE_ALARM.getMessage(), alarmId));
	}

	@ApiOperation("알림 리스트 조회")
	@GetMapping
	public ResponseEntity<ApiResponse> getAlarms(
		@RequestParam(required = false) Integer generation,
		@RequestParam(required = false) Part part,
		@RequestParam(required = false) Status status,
		Pageable pageable
	) {
		val response = alarmService.getAlarms(generation, part, status, pageable);
		return ResponseEntity.ok(success(SUCCESS_GET_ALARMS.getMessage(), response));
	}

	@ApiOperation("알림 상세 조회")
	@GetMapping("/{alarmId}")
	public ResponseEntity<ApiResponse> getAlarm(@PathVariable Long alarmId) {
		val response = alarmService.getAlarm(alarmId);
		return ResponseEntity.ok(success(SUCCESS_GET_ALARM.getMessage(), response));
	}

	@ApiOperation("알림 삭제")
	@DeleteMapping("/{alarmId}")
	public ResponseEntity<ApiResponse> deleteAlarm(@PathVariable Long alarmId) {
		alarmService.deleteAlarm(alarmId);
		return ResponseEntity.ok(success(SUCCESS_DELETE_ALARM.getMessage()));
	}

	private URI getURI(Long alarmId) {
		return ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{alarmId}")
			.buildAndExpand(alarmId)
			.toUri();
	}
}
