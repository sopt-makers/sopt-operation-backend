package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ResponseMessage.*;

import java.net.URI;
import java.security.Principal;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.service.AdminService;
import org.sopt.makers.operation.service.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping
	public ResponseEntity<ApiResponse> getLecturesByGeneration(
		@RequestParam("generation") int generation, Principal principal) {
		adminService.confirmAdmin(Long.valueOf(principal.getName()));
		LectureResponseDTO response = lectureService.getLecturesByGeneration(generation);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_LECTURES.getMessage(), response));
	}

	private URI getURI(Long lectureId) {
		return ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{lectureId}")
			.buildAndExpand(lectureId)
			.toUri();
	}
}
