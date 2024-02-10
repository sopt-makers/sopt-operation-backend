package org.sopt.makers.operation.app.lecture.api;

import static org.sopt.makers.operation.code.success.app.LectureSuccessCode.*;

import java.security.Principal;

import org.sopt.makers.operation.app.lecture.service.LectureService;
import org.sopt.makers.operation.common.dto.BaseResponse;
import org.sopt.makers.operation.common.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/lectures")
public class LectureApiController implements LectureApi {

	private final LectureService lectureService;

	@Override
	@GetMapping
	public ResponseEntity<BaseResponse<?>> getTodayLecture(@NonNull Principal principal) {
		val memberId = Long.parseLong(principal.getName());
		val response = lectureService.getTodayLecture(memberId);
		return ApiResponseUtil.success(SUCCESS_SINGLE_GET_LECTURE, response);
	}

	@Override
	@GetMapping("/round/{lectureId}")
	public ResponseEntity<BaseResponse<?>> getRound(@PathVariable long lectureId) {
		val response = lectureService.getCurrentLectureRound(lectureId);
		return ApiResponseUtil.success(SUCCESS_GET_LECTURE_ROUND, response);
	}
}
