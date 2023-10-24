package org.sopt.makers.operation.controller.web;

import static org.sopt.makers.operation.common.ResponseMessage.*;

import java.net.URI;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.alarm.AlarmRequestDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.entity.alarm.Status;
import org.sopt.makers.operation.service.AlarmService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import lombok.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
public class AlarmController {
	private final AlarmService alarmService;

	@ApiOperation("알림 생성")
	@PostMapping
	public ResponseEntity<ApiResponse> createAlarm(@RequestBody AlarmRequestDTO requestDTO) {
		val alarmId = alarmService.createAlarm(requestDTO);
		return ResponseEntity
			.created(getURI(alarmId))
			.body(ApiResponse.success(SUCCESS_CREATE_ALARM.getMessage(), alarmId));
	}

	@GetMapping
	public ResponseEntity<ApiResponse> getAlarms(
		@RequestParam Integer generation,
		@RequestParam Part part,
		@RequestParam Status status,
		Pageable pageable
	) {
		val response = alarmService.getAlarms(generation, part, status, pageable);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_ALARMS.getMessage(), response));
	}

	private URI getURI(Long alarmId) {
		return ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{alarmId}")
			.buildAndExpand(alarmId)
			.toUri();
	}
}
