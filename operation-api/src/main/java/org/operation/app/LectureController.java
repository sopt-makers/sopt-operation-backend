package org.operation.app;

import static org.sopt.makers.operation.common.ResponseMessage.*;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.dto.ResponseDTO;
import org.sopt.makers.operation.service.app.lecture.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/lectures")
public class LectureController {

    private final LectureService lectureService;

    @ApiOperation(value = "진행 중인 세미나 상태 조회")
    @GetMapping
    public ResponseEntity<ResponseDTO> getLecture(@ApiIgnore Principal principal) {
        val memberPlaygroundId = Long.parseLong(principal.getName());
        val response = lectureService.getTodayLecture(memberPlaygroundId);
        return ResponseEntity.ok(ResponseDTO.success(ResponseMessage.SUCCESS_SINGLE_GET_LECTURE.getMessage(), response));
    }

    @ApiOperation(value = "출석 차수 조회")
    @GetMapping("/round/{lectureId}")
    public ResponseEntity<ResponseDTO> getRound(@PathVariable("lectureId") Long lectureId) {
        val response = lectureService.getCurrentLectureRound(lectureId);
        return ResponseEntity.ok(ResponseDTO.success(ResponseMessage.SUCCESS_GET_LECTURE_ROUND.getMessage(), response));
    }
}
