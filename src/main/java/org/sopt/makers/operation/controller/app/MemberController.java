package org.sopt.makers.operation.controller.app;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.service.app.member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

import static java.util.Objects.nonNull;
import static org.sopt.makers.operation.common.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/members")
public class MemberController {
    private final MemberService memberService;

    @ApiOperation(value = "전체 출석 정보 조회")
    @GetMapping("/attendances")
    public ResponseEntity<ApiResponse> getMemberTotalAttendance(@ApiIgnore Principal principal) {
        val response = memberService.getMemberTotalAttendance(getMemberId(principal));
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_TOTAL_ATTENDANCE.getMessage(), response));
    }

    @ApiOperation(value = "출석 점수 조회")
    @GetMapping("/score")
    public ResponseEntity<ApiResponse> getScore(@ApiIgnore Principal principal) {
        val response = memberService.getMemberScore(getMemberId(principal));
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_ATTENDANCE_SCORE.getMessage(), response));
    }

    private Long getMemberId(Principal principal) {
        return nonNull(principal) ? Long.valueOf(principal.getName()) : null;
    }
}
