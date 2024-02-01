package org.operation.app.attendance;

import static java.util.Objects.*;
import static org.operation.app.attendance.message.SuccessMessage.*;

import java.security.Principal;

import org.operation.app.attendance.dto.request.AttendanceRequest;
import org.operation.common.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/attendances")
public class AttendanceApiController implements AttendanceApi {
	// private final AttendanceService attendanceService;

	@Override
	public ResponseEntity<BaseResponse<?>> attend(@RequestBody AttendanceRequest request, Principal principal) {
		val memberId = getMemberId(principal);
		val response = memberId + ""; // attendanceService.attend(getMemberId(principal), requestDTO);
		return ResponseEntity.ok(BaseResponse.success(SUCCESS_GET_ATTENDANCE.getMessage(), response));
	}

	private Long getMemberId(Principal principal) {
		return nonNull(principal) ? Long.valueOf(principal.getName()) : null;
	}
}
