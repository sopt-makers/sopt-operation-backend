package org.sopt.makers.operation.controller.web;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.dto.ResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.service.web.member.MemberService;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ResponseDTO> getMemberList(
        @RequestParam(required = false) Part part, @RequestParam(required = false) Integer generation, Pageable pageable) {
        val memberList = memberService.getMemberList(part, generation, pageable);
        return ResponseEntity.ok(ResponseDTO.success(SUCCESS_GET_MEMBERS.getMessage(), memberList));
    }
}
