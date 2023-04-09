package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ResponseMessage.*;
import static org.sopt.makers.operation.common.ExceptionMessage.*;

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

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app")
public class AppController {
    private final MemberService memberService;
    private final LectureService lectureService;

    @GetMapping("/lecture")
    public ResponseEntity<ApiResponse> getLecture(Principal principal) {
        Member member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

        LectureGetResponseDTO lectureGetResponseDTO = lectureService.getCurrentLecture(LectureSearchCondition.of(member.getPart(), member.getGeneration(), member.getId()));

        return ResponseEntity
                .ok(ApiResponse.success(SUCCESS_SINGLE_GET_LECTURE.getMessage(), lectureGetResponseDTO));
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse> getMemberTotalAttendance(Principal principal) {
        Member member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

        AttendanceTotalResponseDTO attendanceTotalResponseDTO = memberService.getMemberTotalAttendance(member);

        return ResponseEntity
                .ok(ApiResponse.success(SUCCESS_TOTAL_ATTENDANCE.getMessage(), attendanceTotalResponseDTO));
    }
    
    @GetMapping("/score")
    public ResponseEntity<ApiResponse> getScore(Principal principal) {
        Member member = memberService.confirmMember(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new MemberException(INVALID_MEMBER.getName()));

        return ResponseEntity
                .ok(ApiResponse.success(SUCCESS_GET_ATTENDANCE_SCORE.getMessage(), MemberScoreGetResponse.of(member.getScore())));
    }
}