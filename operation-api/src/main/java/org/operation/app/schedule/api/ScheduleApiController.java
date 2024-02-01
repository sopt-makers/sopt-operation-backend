package org.operation.app.schedule.api;

import static org.operation.app.schedule.message.SuccessMessage.*;

import java.time.LocalDateTime;

import org.operation.common.dto.BaseResponse;
import org.operation.common.util.ApiResponseUtil;
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

	// private final ScheduleService scheduleService;

	@Override
	@GetMapping
	public ResponseEntity<BaseResponse<?>> getSchedules(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
	) {
		val response = ""; // scheduleService.getSchedules(start, end);
		return ApiResponseUtil.ok(SUCCESS_GET_SCHEDULES.getContent(), response);
	}
}
