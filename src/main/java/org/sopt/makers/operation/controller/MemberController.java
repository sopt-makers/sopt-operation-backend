package org.sopt.makers.operation.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.member.MemberListGetResponse;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.service.AdminService;
import org.sopt.makers.operation.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.sopt.makers.operation.common.ResponseMessage.SUCCESS_GET_MEMBERS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final AdminService adminService;
    private final MemberService memberService;
    @ApiOperation(value = "멤버 리스트 조회")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 201, message = "세션 생성 성공"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "유효하지 않은 토큰"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
    })
    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getMemberList(
            @RequestParam(required = false, name = "part") Part part,
            @RequestParam(required = false, name = "generation") Integer generation,
            Principal principal
    ) {
        adminService.confirmAdmin(Long.valueOf(principal.getName()));

        List<MemberListGetResponse> memberList = memberService.getMemberList(part, generation);

        return ResponseEntity
                .ok(ApiResponse.success(SUCCESS_GET_MEMBERS.getMessage(), memberList));
    }
}
