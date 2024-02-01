package org.operation.app.lecture.api;

import static org.operation.app.lecture.message.SuccessMessage.*;

import java.security.Principal;

import org.operation.common.dto.BaseResponse;
import org.operation.common.util.ApiResponseUtil;
import org.operation.common.util.CommonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/lectures")
public class LectureApiController implements LectureApi {

	// private final LectureService lectureService;
	private final CommonUtils utils;

	@Override
	@GetMapping
	public ResponseEntity<BaseResponse<?>> getTodayLecture(@NonNull Principal principal) {
		val memberId = Long.parseLong(principal.getName());
		val response = ""; // lectureService.getTodayLecture(memberId);
		return ApiResponseUtil.ok(SUCCESS_SINGLE_GET_LECTURE.getContent(), response);
	}

	@Override
	@GetMapping("/round/{lectureId}")
	public ResponseEntity<BaseResponse<?>> getRound(@PathVariable long lectureId) {
		val response = ""; // lectureService.getCurrentLectureRound(lectureId);
		return ApiResponseUtil.ok(SUCCESS_GET_LECTURE_ROUND.getContent(), response);
	}
}
