package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ExceptionMessage.INVALID_MEMBER;
import static org.sopt.makers.operation.common.ResponseMessage.*;

import java.net.URI;
import java.security.Principal;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.lecture.*;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.service.AdminService;
import org.sopt.makers.operation.service.LectureService;
import org.sopt.makers.operation.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lectures")
public class LectureController {

	private final AdminService adminService;
	private final LectureService lectureService;
	private final MemberService memberService;

	@ApiOperation(value = "세션 생성")
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code = 201, message = "세션 생성 성공"),
		@io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
		@io.swagger.annotations.ApiResponse(code = 401, message = "유효하지 않은 토큰"),
		@io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
	})
	@PostMapping
	public ResponseEntity<ApiResponse> createLecture(@RequestBody LectureRequestDTO requestDTO, Principal principal) {
		adminService.confirmAdmin(Long.valueOf(principal.getName()));
		Long lectureId = lectureService.createLecture(requestDTO);
		return ResponseEntity
			.created(getURI(lectureId))
			.body(ApiResponse.success(SUCCESS_CREATE_LECTURE.getMessage(), lectureId));
	}

	@ApiOperation(value = "세션 리스트 조회")
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code = 200, message = "세션 리스트 조회 성공"),
		@io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
		@io.swagger.annotations.ApiResponse(code = 401, message = "유효하지 않은 토큰"),
		@io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
	})
	@GetMapping
	public ResponseEntity<ApiResponse> getLecturesByGeneration(
		@RequestParam("generation") int generation, Principal principal) {
		adminService.confirmAdmin(Long.valueOf(principal.getName()));
		LecturesResponseDTO response = lectureService.getLecturesByGeneration(generation);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_LECTURES.getMessage(), response));
	}

	@ApiOperation(value = "세션 상세 조회")
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code = 200, message = "세션 상세 조회 성공"),
		@io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
		@io.swagger.annotations.ApiResponse(code = 401, message = "유효하지 않은 토큰"),
		@io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
	})
	@GetMapping("/{lectureId}")
	public ResponseEntity<ApiResponse> getLecture(@PathVariable("lectureId") Long lectureId,
		@RequestParam(required = false, name = "part") Part part, Principal principal) {
		adminService.confirmAdmin(Long.valueOf(principal.getName()));
		LectureResponseDTO response = lectureService.getLecture(lectureId, part);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_LECTURE.getMessage(), response));
	}

	@ApiOperation(value = "출석 시작")
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code = 200, message = "출석 시작 성공"),
		@io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
		@io.swagger.annotations.ApiResponse(code = 401, message = "유효하지 않은 작큰"),
		@io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
	})
	@PatchMapping("/attendance")
	public ResponseEntity<ApiResponse> startAttendance(
		@RequestBody AttendanceRequestDTO requestDTO, Principal principal) {
		adminService.confirmAdmin(Long.valueOf(principal.getName()));
		AttendanceResponseDTO response = lectureService.startAttendance(requestDTO);
		return ResponseEntity
			.created(getURI(requestDTO.lectureId()))
			.body(ApiResponse.success(SUCCESS_START_ATTENDANCE.getMessage(), response));
	}

	@ApiOperation(value = "출석 점수 갱신 트리거")
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code = 200, message = "출석 시작 성공"),
		@io.swagger.annotations.ApiResponse(code = 400, message = "필요한 값이 없음"),
		@io.swagger.annotations.ApiResponse(code = 401, message = "유효하지 않은 작큰"),
		@io.swagger.annotations.ApiResponse(code = 500, message = "서버 에러")
	})
	@PatchMapping("/{lectureId}")
	public ResponseEntity<ApiResponse> updateMembersScore(
		Principal principal, @PathVariable("lectureId") Long lectureId) {
		adminService.confirmAdmin(Long.valueOf(principal.getName()));
		lectureService.updateMembersScore(lectureId);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_UPDATE_MEMBER_SCORE.getMessage()));
	}

	private URI getURI(Long lectureId) {
		return ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{lectureId}")
			.buildAndExpand(lectureId)
			.toUri();
	}
}
