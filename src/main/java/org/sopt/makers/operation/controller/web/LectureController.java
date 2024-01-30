package org.sopt.makers.operation.controller.web;

import static org.sopt.makers.operation.common.ApiResponse.*;
import static org.sopt.makers.operation.common.ResponseMessage.*;

import java.net.URI;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.lecture.request.AttendanceRequestDTO;
import org.sopt.makers.operation.dto.lecture.request.LectureRequestDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.service.web.lecture.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Tag(name = "Lecture", description = "세션 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lectures")
public class LectureController {
	private final LectureService lectureService;

	@ApiOperation(value = "세션 생성")
	@PostMapping
	public ResponseEntity<ApiResponse> createLecture(@RequestBody LectureRequestDTO requestDTO) {
		val lectureId = lectureService.createLecture(requestDTO);
		return ResponseEntity
			.created(getURI(lectureId))
			.body(success(SUCCESS_CREATE_LECTURE.getMessage(), lectureId));
	}

	@ApiOperation(value = "세션 리스트 조회")
	@GetMapping
	public ResponseEntity<ApiResponse> getLectures(@RequestParam int generation, @RequestParam(required = false) Part part) {
		val response = lectureService.getLectures(generation, part);
		return ResponseEntity.ok(success(SUCCESS_GET_LECTURES.getMessage(), response));
	}

	@ApiOperation(value = "세션 단일 조회")
	@GetMapping("/{lectureId}")
	public ResponseEntity<ApiResponse> getLecture(@PathVariable Long lectureId) {
		val response = lectureService.getLecture(lectureId);
		return ResponseEntity.ok(success(SUCCESS_GET_LECTURE.getMessage(), response));
	}

	@ApiOperation(value = "출석 시작")
	@PatchMapping("/attendance")
	public ResponseEntity<ApiResponse> startAttendance(@RequestBody AttendanceRequestDTO requestDTO) {
		val response = lectureService.startAttendance(requestDTO);
		return ResponseEntity
			.created(getURI(requestDTO.lectureId()))
			.body(success(SUCCESS_START_ATTENDANCE.getMessage(), response));
	}

	private URI getURI(Long lectureId) {
		return ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{lectureId}")
				.buildAndExpand(lectureId)
				.toUri();
	}

	@ApiOperation(value = "세션 종료 후 출석 점수 갱신")
	@PatchMapping("/{lectureId}")
	public ResponseEntity<ApiResponse> endLecture(@PathVariable Long lectureId) {
		lectureService.endLecture(lectureId);
		return ResponseEntity.ok(success(SUCCESS_UPDATE_MEMBER_SCORE.getMessage()));
	}

	@ApiOperation(value = "세션 삭제")
	@DeleteMapping("/{lectureId}")
	public ResponseEntity<ApiResponse> deleteLecture(@PathVariable Long lectureId) {
		lectureService.deleteLecture(lectureId);
		return ResponseEntity.ok(success(SUCCESS_DELETE_LECTURE.getMessage()));
	}

	@ApiOperation(value = "세션 팝업용 상세 조회")
	@GetMapping("/detail/{lectureId}")
	public ResponseEntity<ApiResponse> getLectureDetail(@PathVariable Long lectureId) {
		val response = lectureService.getLectureDetail(lectureId);
		return ResponseEntity.ok(success(SUCCESS_GET_LECTURE.getMessage(), response));
	}
}
