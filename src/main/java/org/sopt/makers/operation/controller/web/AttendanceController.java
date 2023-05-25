package org.sopt.makers.operation.controller.web;

import static org.sopt.makers.operation.common.ResponseMessage.*;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.attendance.AttendUpdateRequestDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.service.AttendanceService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendances")
public class AttendanceController {
	private final AttendanceService attendanceService;

	@ApiOperation(value = "출석 상태 변경")
	@PatchMapping
	public ResponseEntity<ApiResponse> updateAttendanceStatus(@RequestBody AttendUpdateRequestDTO requestDTO) {
		val response = attendanceService.updateAttendanceStatus(requestDTO);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_UPDATE_ATTENDANCE_STATUS.getMessage(), response));
	}

	@ApiOperation(value = "유저별 출석 정보 조회")
	@GetMapping("/{memberId}")
	public ResponseEntity<ApiResponse> findMemberAttendance(@PathVariable Long memberId) {
		val response = attendanceService.findMemberAttendance(memberId);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_MEMBER_ATTENDANCE.getMessage(), response));
	}

	@ApiOperation(value = "출석 점수 갱신 성공")
	@PatchMapping("/member/{memberId}")
	public ResponseEntity<ApiResponse> updateMemberScore(@PathVariable Long memberId) {
		val response = attendanceService.updateMemberScore(memberId);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_UPDATE_MEMBER_SCORE.getMessage(), response));
	}

	@ApiOperation(value = "세션별 출석 정보 조회")
	@GetMapping("/lecture/{lectureId}")
	public ResponseEntity<ApiResponse> getAttendancesByLecture(
		@PathVariable Long lectureId, @RequestParam(required = false) Part part, Pageable pageable) {
		val response = attendanceService.getMemberAttendances(lectureId, part, pageable);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_ATTENDANCES.getMessage(), response));
	}
}
