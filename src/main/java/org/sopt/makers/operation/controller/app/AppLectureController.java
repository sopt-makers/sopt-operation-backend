package org.sopt.makers.operation.controller.app;


import static java.util.Objects.*;
import static org.sopt.makers.operation.common.ResponseMessage.*;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.service.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/lectures")
public class AppLectureController {
    private final LectureService lectureService;
    @ApiOperation(value = "단일 세미나 상태 조회")
    @GetMapping
    public ResponseEntity<ApiResponse> getLecture(@ApiIgnore Principal principal) {
        val response = lectureService.getCurrentLecture(getMemberId(principal));
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_SINGLE_GET_LECTURE.getMessage(), response));
    }

    @ApiOperation(value = "출석 차수 조회")
    @GetMapping("/round/{lectureId}")
    public ResponseEntity<ApiResponse> getRound(@PathVariable("lectureId") Long lectureId) {
        val response = lectureService.getCurrentLectureRound(lectureId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_LECTURE_ROUND.getMessage(), response));
    }

    private Long getMemberId(Principal principal) {
        return nonNull(principal) ? Long.valueOf(principal.getName()) : null;
    }
}
