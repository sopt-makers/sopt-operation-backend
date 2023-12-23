package org.sopt.makers.operation.controller.app;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static org.sopt.makers.operation.common.ApiResponse.success;
import static org.sopt.makers.operation.common.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/schedules")
public class AppScheduleController {
    private final ScheduleService scheduleService;

    @ApiOperation("일정 리스트 조회")
    @GetMapping
    public ResponseEntity<ApiResponse> getAlarms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        val response = scheduleService.getSchedules(start, end);
        return ResponseEntity.ok(success(SUCCESS_GET_SCHEDULES.getMessage(), response));
    }
}
