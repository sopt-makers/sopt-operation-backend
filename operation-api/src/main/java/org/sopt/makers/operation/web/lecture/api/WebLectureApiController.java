package org.sopt.makers.operation.web.lecture.api;

import static org.sopt.makers.operation.code.success.web.LectureSuccessCode.*;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.web.lecture.dto.request.AttendanceRequest;
import org.sopt.makers.operation.web.lecture.dto.request.LectureRequest;
import org.sopt.makers.operation.web.lecture.service.WebLectureService;
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

import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lectures")
public class WebLectureApiController implements WebLectureApi {

	private final WebLectureService lectureService;

	@Override
	@PostMapping
	public ResponseEntity<BaseResponse<?>> createLecture(@RequestBody LectureRequest request) {
		val lectureId = lectureService.createLecture(request);
		return ApiResponseUtil.success(SUCCESS_CREATE_LECTURE, lectureId);
	}

	@Override
	@GetMapping
	public ResponseEntity<BaseResponse<?>> getLectures(
			@RequestParam int generation,
			@RequestParam(required = false) Part part
	) {
		val response = lectureService.getLectures(generation, part);
		return ApiResponseUtil.success(SUCCESS_GET_LECTURES, response);
	}

	@Override
	@GetMapping("/{lectureId}")
	public ResponseEntity<BaseResponse<?>> getLecture(@PathVariable long lectureId) {
		val response = lectureService.getLecture(lectureId);
		return ApiResponseUtil.success(SUCCESS_GET_LECTURE, response);
	}

	@Override
	@PatchMapping("/attendance")
	public ResponseEntity<BaseResponse<?>> startAttendance(AttendanceRequest request) {
		val response = lectureService.startAttendance(request);
		return ApiResponseUtil.success(SUCCESS_START_ATTENDANCE, response);
	}

	@Override
	@PatchMapping("/{lectureId}")
	public ResponseEntity<BaseResponse<?>> endLecture(@PathVariable long lectureId) {
		lectureService.endLecture(lectureId);
		return ApiResponseUtil.success(SUCCESS_UPDATE_MEMBER_SCORE);
	}

	@Override
	@DeleteMapping("/{lectureId}")
	public ResponseEntity<BaseResponse<?>> deleteLecture(@PathVariable long lectureId) {
		lectureService.deleteLecture(lectureId);
		return ApiResponseUtil.success(SUCCESS_DELETE_LECTURE);
	}

	@Override
	@GetMapping("/detail/{lectureId}")
	public ResponseEntity<BaseResponse<?>> getLectureDetail(@PathVariable long lectureId) {
		val response = lectureService.getLectureDetail(lectureId);
		return ApiResponseUtil.success(SUCCESS_GET_LECTURE, response);
	}
}
