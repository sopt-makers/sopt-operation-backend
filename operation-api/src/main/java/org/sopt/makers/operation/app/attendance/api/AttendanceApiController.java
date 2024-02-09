package org.sopt.makers.operation.app.attendance.api;

import static org.sopt.makers.operation.code.success.app.AttendanceSuccessCode.*;

import java.security.Principal;

import org.sopt.makers.operation.app.attendance.dto.request.AttendanceRequest;
import org.sopt.makers.operation.app.attendance.service.AttendanceService;
import org.sopt.makers.operation.common.util.CommonUtils;
import org.sopt.makers.operation.common.util.ApiResponseUtil;
import org.sopt.makers.operation.common.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/attendances")
public class AttendanceApiController implements AttendanceApi {

	private final AttendanceService attendanceService;
	private final CommonUtils utils;

	@Override
	@PostMapping("/attend")
	public ResponseEntity<BaseResponse<?>> attend(@RequestBody AttendanceRequest request, @NonNull Principal principal) {
		val memberId = utils.getMemberId(principal);
		val response = attendanceService.attend(memberId, request);
		return ApiResponseUtil.success(SUCCESS_ATTEND, response);
	}
}
