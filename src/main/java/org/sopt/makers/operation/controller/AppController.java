package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ResponseMessage.*;
import static org.sopt.makers.operation.common.ExceptionMessage.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.attendance.AttendanceTotalResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureGetResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureSearchCondition;
import org.sopt.makers.operation.dto.member.MemberScoreGetResponse;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.exception.MemberException;
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

    @ApiOperation(value = "단일 세미나 상태 조회")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "세션 조회 성공"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "유효하지 않은 토큰"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
    })
    @GetMapping("/lecture")
    public ResponseEntity<ApiResponse> getLecture(@ApiIgnore Principal principal) {
        Member member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

        LectureGetResponseDTO lectureGetResponseDTO = lectureService.getCurrentLecture(LectureSearchCondition.of(member.getPart(), member.getGeneration(), member.getId()));

        return ResponseEntity
                .ok(ApiResponse.success(SUCCESS_SINGLE_GET_LECTURE.getMessage(), lectureGetResponseDTO));
    }

    @ApiOperation(value = "전체 출석 정보 조회")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "전체 출석정보 조회 성공"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "유효하지 않은 토큰"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
    })
    @GetMapping("/total")
    public ResponseEntity<ApiResponse> getMemberTotalAttendance(@ApiIgnore Principal principal) {
        Member member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

        AttendanceTotalResponseDTO attendanceTotalResponseDTO = memberService.getMemberTotalAttendance(member);

        return ResponseEntity
                .ok(ApiResponse.success(SUCCESS_TOTAL_ATTENDANCE.getMessage(), attendanceTotalResponseDTO));
    }

    @ApiOperation(value = "출석 점수 조회")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "출석 점수 조회 성공"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "유효하지 않은 토큰"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
    })
    @GetMapping("/score")
    public ResponseEntity<ApiResponse> getScore(@ApiIgnore Principal principal) {
        Member member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

        return ResponseEntity
                .ok(ApiResponse.success(SUCCESS_GET_ATTENDANCE_SCORE.getMessage(), MemberScoreGetResponse.of(member.getScore())));
    }
}