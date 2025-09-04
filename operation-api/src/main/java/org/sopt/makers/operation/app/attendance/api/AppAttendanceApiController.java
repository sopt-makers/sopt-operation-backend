package org.sopt.makers.operation.app.attendance.api;

import static org.sopt.makers.operation.code.success.app.AttendanceSuccessCode.*;

import java.security.Principal;

import org.sopt.makers.operation.app.attendance.dto.request.LectureAttendRequest;
import org.sopt.makers.operation.app.attendance.service.AppAttendanceService;
import org.sopt.makers.operation.authentication.AdminAuthentication;
import org.sopt.makers.operation.authentication.MakersAuthentication;
import org.sopt.makers.operation.common.util.CommonUtils;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/attendances")
public class AppAttendanceApiController implements AppAttendanceApi {

	private final AppAttendanceService attendanceService;
	private final CommonUtils utils;

	@Override
	@PostMapping("/attend")
	public ResponseEntity<BaseResponse<?>> attend(@RequestBody LectureAttendRequest request, Principal principal) {
		//val memberId = utils.getMemberId(principal);
        Long memberId = extractUserId(principal);

		val response = attendanceService.attend(memberId, request);
		return ApiResponseUtil.success(SUCCESS_ATTEND, response);
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
