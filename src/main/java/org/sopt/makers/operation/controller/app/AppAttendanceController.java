package org.sopt.makers.operation.controller.app;

import static java.util.Objects.*;
import static org.sopt.makers.operation.common.ResponseMessage.*;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.attendance.AttendRequestDTO;
import org.sopt.makers.operation.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/attendances")
public class AppAttendanceController {
    private final AttendanceService attendanceService;
    @ApiOperation(value = "출석 하기")
    @PostMapping("/attend")
    public ResponseEntity<ApiResponse> attend(@RequestBody AttendRequestDTO requestDTO, @ApiIgnore Principal principal) {
        val response = attendanceService.attend(getMemberId(principal), requestDTO);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_ATTEND.getMessage(), response));
    }

    private Long getMemberId(Principal principal) {
        return nonNull(principal) ? Long.valueOf(principal.getName()) : null;
    }
}
