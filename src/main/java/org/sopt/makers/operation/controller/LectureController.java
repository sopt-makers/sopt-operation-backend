package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ResponseMessage.*;

import java.net.URI;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.lecture.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.AttendanceResponseDTO;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.service.LectureService;
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
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lectures")
public class LectureController {
	private final LectureService lectureService;

	@ApiOperation(value = "세션 생성")
	@PostMapping
	public ResponseEntity<ApiResponse> createLecture(@RequestBody LectureRequestDTO requestDTO) {
		Long lectureId = lectureService.createLecture(requestDTO);
		return ResponseEntity
			.created(getURI(lectureId))
			.body(ApiResponse.success(SUCCESS_CREATE_LECTURE.getMessage(), lectureId));
	}

	@ApiOperation(value = "세션 리스트 조회")
	@GetMapping
	public ResponseEntity<ApiResponse> getLecturesByGeneration(
		@RequestParam("generation") int generation, @RequestParam(required = false) Part part) {
		LecturesResponseDTO response = lectureService.getLecturesByGeneration(generation, part);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_LECTURES.getMessage(), response));
	}

	@ApiOperation(value = "세션 상세 조회")
	@GetMapping("/{lectureId}")
	public ResponseEntity<ApiResponse> getLecture(@PathVariable Long lectureId) {
		LectureResponseDTO response = lectureService.getLecture(lectureId);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_LECTURE.getMessage(), response));
	}

	@ApiOperation(value = "출석 시작")
	@PatchMapping("/attendance")
	public ResponseEntity<ApiResponse> startAttendance(@RequestBody AttendanceRequestDTO requestDTO) {
		AttendanceResponseDTO response = lectureService.startAttendance(requestDTO);
		return ResponseEntity
			.created(getURI(requestDTO.lectureId()))
			.body(ApiResponse.success(SUCCESS_START_ATTENDANCE.getMessage(), response));
	}

	@ApiOperation(value = "출석 점수 갱신 트리거")
	@PatchMapping("/{lectureId}")
	public ResponseEntity<ApiResponse> updateMembersScore(@PathVariable("lectureId") Long lectureId) {
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
