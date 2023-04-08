package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ResponseMessage.*;

import java.security.Principal;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.attendance.AttendanceMemberResponseDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.attendance.AttendanceResponseDTO;
import org.sopt.makers.operation.service.AdminService;
import org.sopt.makers.operation.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendances")
public class AttendanceController {

	private final AdminService adminService;
	private final AttendanceService attendanceService;

	@ApiOperation(value = "출석 상태 변경")
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code = 201, message = "출석 상태 변경 성공"),
		@io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
		@io.swagger.annotations.ApiResponse(code = 401, message = "유효하지 않은 토큰"),
		@io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
	})
	@PatchMapping
	public ResponseEntity<ApiResponse> updateAttendanceStatus(
		@RequestBody AttendanceRequestDTO requestDTO, Principal principal) {
		adminService.confirmAdmin(Long.valueOf(principal.getName()));
		AttendanceResponseDTO response = attendanceService.updateAttendanceStatus(requestDTO);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_UPDATE_ATTENDANCE_STATUS.getMessage(), response));
	}

	@ApiOperation(value = "유저별 출석 정보 조회")
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code = 201, message = "유저 출석 조회 성공"),
		@io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
		@io.swagger.annotations.ApiResponse(code = 401, message = "유효하지 않은 토큰"),
		@io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
	})
	@GetMapping("/{memberId}")
	public ResponseEntity<ApiResponse> updateAttendanceStatus(
		@PathVariable("memberId") Long memberId, Principal principal) {
		adminService.confirmAdmin(Long.valueOf(principal.getName()));
		AttendanceMemberResponseDTO response = attendanceService.getMemberAttendance(memberId);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_MEMBER_ATTENDANCE.getMessage(), response));
	}
}
