package org.sopt.makers.operation.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.sopt.makers.operation.common.ResponseMessage.SUCCESS_GET_MEMBERS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    @ApiOperation(value = "멤버 리스트 조회")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getMemberList(
        @RequestParam(required = false) Part part, @RequestParam(required = false) Integer generation) {
        val memberList = memberService.getMemberList(part, generation);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_MEMBERS.getMessage(), memberList));
    }
}
