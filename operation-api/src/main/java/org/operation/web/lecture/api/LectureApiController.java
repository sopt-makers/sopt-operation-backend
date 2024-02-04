package org.operation.web.lecture.api;

import static org.operation.web.lecture.message.SuccessMessage.*;

import org.operation.common.domain.Part;
import org.operation.common.dto.BaseResponse;
import org.operation.common.util.ApiResponseUtil;
import org.operation.common.util.CommonUtils;
import org.operation.web.lecture.dto.request.AttendanceRequest;
import org.operation.web.lecture.dto.request.LectureRequest;
import org.operation.web.lecture.service.LectureService;
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
public class LectureApiController implements LectureApi {

	private final LectureService lectureService;
	private final CommonUtils utils;

	@Override
	@PostMapping
	public ResponseEntity<BaseResponse<?>> createLecture(@RequestBody LectureRequest request) {
		val lectureId = lectureService.createLecture(request);
		val uri = utils.getURI("/{lectureId}", lectureId);
		return ApiResponseUtil.created(uri, SUCCESS_CREATE_LECTURE.getContent(), lectureId);
	}

	@Override
	@GetMapping
	public ResponseEntity<BaseResponse<?>> getLectures(
			@RequestParam int generation,
			@RequestParam(required = false) Part part
	) {
		val response = lectureService.getLectures(generation, part);
		return ApiResponseUtil.ok(SUCCESS_GET_LECTURES.getContent(), response);
	}

	@Override
	@GetMapping("/{lectureId}")
	public ResponseEntity<BaseResponse<?>> getLecture(@PathVariable long lectureId) {
		val response = lectureService.getLecture(lectureId);
		return ApiResponseUtil.ok(SUCCESS_GET_LECTURE.getContent(), response);
	}

	@Override
	@PatchMapping("/attendance")
	public ResponseEntity<BaseResponse<?>> startAttendance(AttendanceRequest request) {
		val response = lectureService.startAttendance(request);
		val uri = utils.getURI("/{lectureId}", response.lectureId());
		return ApiResponseUtil.created(uri, SUCCESS_START_ATTENDANCE.getContent(), response);
	}

	@Override
	@PatchMapping("/{lectureId}")
	public ResponseEntity<BaseResponse<?>> endLecture(@PathVariable long lectureId) {
		lectureService.endLecture(lectureId);
		return ApiResponseUtil.ok(SUCCESS_UPDATE_MEMBER_SCORE.getContent(), lectureService);
	}

	@Override
	@DeleteMapping("/{lectureId}")
	public ResponseEntity<BaseResponse<?>> deleteLecture(@PathVariable long lectureId) {
		lectureService.deleteLecture(lectureId);
		return ApiResponseUtil.ok(SUCCESS_DELETE_LECTURE.getContent());
	}

	@Override
	@GetMapping("/detail/{lectureId}")
	public ResponseEntity<BaseResponse<?>> getLectureDetail(@PathVariable long lectureId) {
		val response = lectureService.getLectureDetail(lectureId);
		return ApiResponseUtil.ok(SUCCESS_GET_LECTURE.getContent(), response);
	}
}
