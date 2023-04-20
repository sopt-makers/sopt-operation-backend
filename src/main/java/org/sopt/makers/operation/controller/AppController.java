package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ResponseMessage.*;
import static org.sopt.makers.operation.common.ExceptionMessage.*;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.attendance.AttendRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureGetResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureSearchCondition;
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
        val member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

        LectureGetResponseDTO lectureGetResponseDTO = lectureService.getCurrentLecture(LectureSearchCondition.of(member.getPart(), member.getGeneration(), member.getId()));

        return ResponseEntity
                .ok(ApiResponse.success(SUCCESS_SINGLE_GET_LECTURE.getMessage(), lectureGetResponseDTO));
    }

    @ApiOperation(value = "전체 출석 정보 조회")
    @GetMapping("/total")
    public ResponseEntity<ApiResponse> getMemberTotalAttendance(@ApiIgnore Principal principal) {
        val member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

        AttendanceTotalResponseDTO attendanceTotalResponseDTO = memberService.getMemberTotalAttendance(member);

        return ResponseEntity
                .ok(ApiResponse.success(SUCCESS_TOTAL_ATTENDANCE.getMessage(), attendanceTotalResponseDTO));
    }

    @ApiOperation(value = "출석 점수 조회")
    @GetMapping("/score")
    public ResponseEntity<ApiResponse> getScore(@ApiIgnore Principal principal) {
        val member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

        return ResponseEntity
                .ok(ApiResponse.success(SUCCESS_GET_ATTENDANCE_SCORE.getMessage(), MemberScoreGetResponse.of(member.getScore())));
    }

    @ApiOperation(value = "출석 차수 조회")
    @GetMapping("/lecture/round/{lectureId}")
    public ResponseEntity<ApiResponse> getRound(@PathVariable("lectureId") Long lectureId) {
        val lectureCurrentRoundResponseDTO = lectureService.getCurrentLectureRound(lectureId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_LECUTRE_ROUND.getMessage(), lectureCurrentRoundResponseDTO));
    }

    @ApiOperation(value = "출석 하기")
    @PostMapping("/attend")
    public ResponseEntity<ApiResponse> attend(@RequestBody AttendRequestDTO requestDTO, @ApiIgnore Principal principal) {
        Member member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

        val attendResponseDTO = attendanceService.attend(member.getId(), requestDTO);

        return ResponseEntity.ok(ApiResponse.success(SUCCESS_ATTEND.getMessage(), attendResponseDTO));
    }
}