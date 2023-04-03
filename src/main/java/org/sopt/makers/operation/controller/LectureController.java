package org.sopt.makers.operation.controller;

import java.security.Principal;

import org.sopt.makers.operation.common.ApiResponse;
import org.sopt.makers.operation.dto.LectureRequestDTO;
import org.sopt.makers.operation.entity.Lecture;
import org.sopt.makers.operation.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lectures")
public class LectureController {

	private final AuthService authService;

	@PostMapping
	public ResponseEntity<ApiResponse> createLecture(@RequestBody LectureRequestDTO requestDTO, Principal principal) {
		authService.confirmAdmin(Long.valueOf(principal.getName()));

	}
}
