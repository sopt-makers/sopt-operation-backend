package org.sopt.makers.operation.app.member.api;

import static org.sopt.makers.operation.code.success.app.MemberSuccessCode.*;

import java.security.Principal;

import org.sopt.makers.operation.app.member.service.AppMemberService;
import org.sopt.makers.operation.authentication.AdminAuthentication;
import org.sopt.makers.operation.authentication.MakersAuthentication;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.common.util.CommonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/members")
public class AppMemberApiController implements AppMemberApi {

	private final AppMemberService memberService;
	private final CommonUtils utils;

	@Override
	@GetMapping("/attendances")
	public ResponseEntity<BaseResponse<?>> getMemberTotalAttendance(@NonNull Principal principal) {
        Long memberId = extractUserId(principal);


		val response = memberService.getMemberTotalAttendance(memberId);
		return ApiResponseUtil.success(SUCCESS_GET_TOTAL_ATTENDANCE, response);
	}

	@Override
	@GetMapping("/score")
	public ResponseEntity<BaseResponse<?>> getScore(@NonNull Principal principal) {
        Long memberId = extractUserId(principal);

		val response = memberService.getMemberScore(memberId);
		return ApiResponseUtil.success(SUCCESS_GET_ATTENDANCE_SCORE, response);
	}


    private Long extractUserId(Principal principal) {
        // 외부 JWK 토큰인 경우 - MakersAuthentication
        if (principal instanceof MakersAuthentication makers) {
            return Long.parseLong(makers.getUserId());
        }

        // 내부 APP_ACCESS_TOKEN인 경우 - AdminAuthentication (playgroundId 포함)
        if (principal instanceof AdminAuthentication admin) {
            return (Long) admin.getPrincipal(); // playgroundId가 저장됨
        }

        // 기본 fallback (기존 방식)
        return Long.parseLong(principal.getName());
    }

}
