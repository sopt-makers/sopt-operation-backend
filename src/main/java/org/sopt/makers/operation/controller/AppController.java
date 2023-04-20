package org.sopt.makers.operation.controller;

import static java.util.Objects.*;
import static org.sopt.makers.operation.common.ResponseMessage.*;
import static org.sopt.makers.operation.common.ExceptionMessage.*;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.attendance.AttendRequestDTO;
import org.sopt.makers.operation.dto.member.MemberScoreGetResponse;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.service.AttendanceService;
import org.sopt.makers.operation.service.LectureService;
import org.sopt.makers.operation.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app")
public class AppController {
    private final MemberService memberService;
    private final LectureService lectureService;
    private final AttendanceService attendanceService;

    @ApiOperation(value = "단일 세미나 상태 조회")
    @GetMapping("/lecture")
    public ResponseEntity<ApiResponse> getLecture(@ApiIgnore Principal principal) {
        Long memberId = getMemberId(principal);
        val response = lectureService.getCurrentLecture(memberId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_SINGLE_GET_LECTURE.getMessage(), response));
    }

    @ApiOperation(value = "전체 출석 정보 조회")
    @GetMapping("/total")
    public ResponseEntity<ApiResponse> getMemberTotalAttendance(@ApiIgnore Principal principal) {
        // TODO: memberId를 Service 로 넘기기 제안
        val member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));
        val response = memberService.getMemberTotalAttendance(member);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_TOTAL_ATTENDANCE.getMessage(), response));
    }

    @ApiOperation(value = "출석 점수 조회")
    @GetMapping("/score")
    public ResponseEntity<ApiResponse> getScore(@ApiIgnore Principal principal) {
        //TODO: memberId를 Service 계층으로 넘기는 것은 어떨까
        val member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));
        val response = MemberScoreGetResponse.of(member.getScore());
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_ATTENDANCE_SCORE.getMessage(), response));
    }

    @ApiOperation(value = "출석 차수 조회")
    @GetMapping("/lecture/round/{lectureId}")
    public ResponseEntity<ApiResponse> getRound(@PathVariable("lectureId") Long lectureId) {
        val response = lectureService.getCurrentLectureRound(lectureId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_LECTURE_ROUND.getMessage(), response));
    }

    @ApiOperation(value = "출석 하기")
    @PostMapping("/attend")
    public ResponseEntity<ApiResponse> attend(
        @RequestBody AttendRequestDTO requestDTO, @ApiIgnore Principal principal) {
        //TODO: memberId 넘기기 제안
        Member member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));
        val response = attendanceService.attend(member.getId(), requestDTO);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_ATTEND.getMessage(), response));
    }

    private Long getMemberId(Principal principal) {
        return nonNull(principal) ? Long.valueOf(principal.getName()) : null;
    }
}