package org.sopt.makers.operation.app.schedule.api;

import static org.sopt.makers.operation.code.success.app.ScheduleSuccessCode.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.*;

import java.time.LocalDateTime;

import org.sopt.makers.operation.app.schedule.service.ScheduleService;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/schedules")
public class ScheduleApiController implements ScheduleApi {

	private final ScheduleService scheduleService;

	@Override
	@GetMapping
	public ResponseEntity<BaseResponse<?>> getSchedules(
			@RequestParam @DateTimeFormat(iso = DATE_TIME) LocalDateTime start,
			@RequestParam @DateTimeFormat(iso = DATE_TIME) LocalDateTime end
	) {
		val response = scheduleService.getSchedules(start, end);
		return ApiResponseUtil.success(SUCCESS_GET_SCHEDULES, response);
	}
}
