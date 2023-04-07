package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ResponseMessage.*;
import static org.sopt.makers.operation.common.ExceptionMessage.*;

import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.lecture.LectureGetResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureSearchCondition;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.exception.MemberException;
import org.sopt.makers.operation.service.LectureService;
import org.sopt.makers.operation.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

        LectureGetResponseDTO lectureGetResponseDTO = lectureService.getCurrentLecture(new LectureSearchCondition(member.getPart(),member.getGeneration()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SUCCESS_GET_LECTURE.getMessage(), lectureGetResponseDTO));
    }
}
