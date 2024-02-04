package org.sopt.makers.operation.web.attendnace.api;

import static org.sopt.makers.operation.web.attendnace.message.SuccessMessage.*;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.common.dto.BaseResponse;
import org.sopt.makers.operation.common.util.ApiResponseUtil;
import org.sopt.makers.operation.web.attendance.dto.request.SubAttendanceUpdateRequest;
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
	public ResponseEntity<BaseResponse<?>> updateSubAttendance(@RequestBody SubAttendanceUpdateRequest request) {
		val response = attendanceService.updateSubAttendance(request);
		return ApiResponseUtil.ok(SUCCESS_UPDATE_ATTENDANCE_STATUS.getContent(), response);
	}

	@Override
	@GetMapping("/{memberId}")
	public ResponseEntity<BaseResponse<?>> findAttendancesByMember(@PathVariable long memberId) {
		val response = attendanceService.findAttendancesByMember(memberId);
		return ApiResponseUtil.ok(SUCCESS_GET_MEMBER_ATTENDANCE.getContent(), response);
	}

	@Override
	@PatchMapping("/member/{memberId}")
	public ResponseEntity<BaseResponse<?>> updateMemberScore(@PathVariable long memberId) {
		val response = attendanceService.updateMemberScore(memberId);
		return ApiResponseUtil.ok(SUCCESS_UPDATE_MEMBER_SCORE.getContent(), response);
	}

	@Override
	@GetMapping("/lecture/{lectureId}")
	public ResponseEntity<BaseResponse<?>> findAttendancesByLecture(
			@PathVariable long lectureId,
			@RequestParam(required = false) Part part,
			Pageable pageable
	) {
		val response = attendanceService.findAttendancesByLecture(lectureId, part, pageable);
		return ApiResponseUtil.ok(SUCCESS_GET_ATTENDANCES.getContent(), response);
	}
}
