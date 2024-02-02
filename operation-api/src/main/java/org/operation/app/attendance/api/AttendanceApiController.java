package org.operation.app.attendance.api;

import static org.operation.app.attendance.message.SuccessMessage.*;

import java.security.Principal;

import org.operation.app.attendance.dto.request.AttendanceRequest;
import org.operation.app.attendance.service.AttendanceService;
import org.operation.common.util.CommonUtils;
import org.operation.common.util.ApiResponseUtil;
import org.operation.common.dto.BaseResponse;
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
		return ApiResponseUtil.ok(SUCCESS_GET_ATTENDANCE.getContent(), response);
	}
}
