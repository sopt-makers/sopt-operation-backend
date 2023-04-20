package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ResponseMessage.*;

import java.util.List;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.attendance.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.attendance.MemberResponseDTO;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendances")
public class AttendanceController {
	private final AttendanceService attendanceService;

	@ApiOperation(value = "출석 상태 변경")
	@PatchMapping
	public ResponseEntity<ApiResponse> updateAttendanceStatus(@RequestBody AttendanceRequestDTO requestDTO) {
		AttendanceResponseDTO response = attendanceService.updateAttendanceStatus(requestDTO);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_UPDATE_ATTENDANCE_STATUS.getMessage(), response));
	}

	@ApiOperation(value = "유저별 출석 정보 조회")
	@GetMapping("/{memberId}")
	public ResponseEntity<ApiResponse> updateAttendanceStatus(
		@PathVariable("memberId") Long memberId) {
		AttendanceMemberResponseDTO response = attendanceService.getMemberAttendance(memberId);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_MEMBER_ATTENDANCE.getMessage(), response));
	}

	@PatchMapping("/member/{memberId}")
	public ResponseEntity<ApiResponse> updateMemberScore(@PathVariable Long memberId) {
		float response = attendanceService.updateMemberScore(memberId);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_UPDATE_MEMBER_SCORE.getMessage(), response));
	}

	@ApiOperation(value = "세션별 출석 정보 조회")
	@GetMapping("lecture/{lectureId}")
	public ResponseEntity<ApiResponse> getAttendancesByLecture(
		@PathVariable Long lectureId, @RequestParam(required = false) Part part, Pageable pageable) {
		List<MemberResponseDTO> response = attendanceService.getMemberAttendances(lectureId, part, pageable);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_ATTENDANCES.getMessage(), response));
	}
}
