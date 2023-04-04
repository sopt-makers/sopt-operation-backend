package org.sopt.makers.operation.controller;

import static org.sopt.makers.operation.common.ResponseMessage.*;

import java.net.URI;
import java.security.Principal;

import javax.websocket.server.PathParam;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.lecture.LectureRequestDTO;
import org.sopt.makers.operation.dto.lecture.LectureResponseDTO;
import org.sopt.makers.operation.dto.lecture.LecturesResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.sopt.makers.operation.service.AdminService;
import org.sopt.makers.operation.service.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lectures")
public class LectureController {

	private final AdminService adminService;
	private final LectureService lectureService;

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
		LecturesResponseDTO response = lectureService.getLecturesByGeneration(generation);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_LECTURES.getMessage(), response));
	}

	@GetMapping("/{lectureId}")
	public ResponseEntity<ApiResponse> getLecture(@PathVariable("lectureId") Long lectureId,
		@RequestParam(required = false, name = "part") Part part, Principal principal) {
		adminService.confirmAdmin(Long.valueOf(principal.getName()));
		LectureResponseDTO response = lectureService.getLecture(lectureId, part);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_LECTURE.getMessage(), response));
	}

	private URI getURI(Long lectureId) {
		return ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{lectureId}")
			.buildAndExpand(lectureId)
			.toUri();
	}
}
