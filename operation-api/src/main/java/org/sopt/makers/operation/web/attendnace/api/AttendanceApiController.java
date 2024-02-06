package org.sopt.makers.operation.web.attendnace.api;

import static org.sopt.makers.operation.code.success.web.AttendanceSuccessCode.*;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.common.dto.BaseResponse;
import org.sopt.makers.operation.common.util.ApiResponseUtil;
import org.sopt.makers.operation.web.attendnace.dto.request.UpdatedSubAttendanceRequest;
import org.sopt.makers.operation.web.attendnace.service.AttendanceService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendances")
public class AttendanceApiController implements AttendanceApi {

	private final AttendanceService attendanceService;

	@Override
	@PatchMapping
	public ResponseEntity<BaseResponse<?>> updateSubAttendance(@RequestBody UpdatedSubAttendanceRequest request) {
		val response = attendanceService.updateSubAttendance(request);
		return ApiResponseUtil.success(SUCCESS_UPDATE_ATTENDANCE_STATUS, response);
	}

	@Override
	@GetMapping("/{memberId}")
	public ResponseEntity<BaseResponse<?>> findAttendancesByMember(@PathVariable long memberId) {
		val response = attendanceService.findAttendancesByMember(memberId);
		return ApiResponseUtil.success(SUCCESS_GET_MEMBER_ATTENDANCE, response);
	}

	@Override
	@PatchMapping("/member/{memberId}")
	public ResponseEntity<BaseResponse<?>> updateMemberScore(@PathVariable long memberId) {
		val response = attendanceService.updateMemberAllScore(memberId);
		return ApiResponseUtil.success(SUCCESS_UPDATE_MEMBER_SCORE, response);
	}

	@Override
	@GetMapping("/lecture/{lectureId}")
	public ResponseEntity<BaseResponse<?>> findAttendancesByLecture(
			@PathVariable long lectureId,
			@RequestParam(required = false) Part part,
			Pageable pageable
	) {
		val response = attendanceService.findAttendancesByLecture(lectureId, part, pageable);
		return ApiResponseUtil.success(SUCCESS_GET_ATTENDANCES, response);
	}
}
