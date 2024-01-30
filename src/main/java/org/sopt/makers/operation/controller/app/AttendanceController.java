package org.sopt.makers.operation.controller.app;

import static java.util.Objects.*;
import static org.sopt.makers.operation.common.ResponseMessage.*;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.dto.ResponseDTO;
import org.sopt.makers.operation.dto.attendance.request.AttendRequestDTO;
import org.sopt.makers.operation.service.app.attendance.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/attendances")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @ApiOperation(value = "출석 하기")
    @PostMapping("/attend")
    public ResponseEntity<ResponseDTO> attend(@RequestBody AttendRequestDTO requestDTO, @ApiIgnore Principal principal) {
        val response = attendanceService.attend(getMemberId(principal), requestDTO);
        return ResponseEntity.ok(ResponseDTO.success(SUCCESS_ATTEND.getMessage(), response));
    }

    private Long getMemberId(Principal principal) {
        return nonNull(principal) ? Long.valueOf(principal.getName()) : null;
    }
}
