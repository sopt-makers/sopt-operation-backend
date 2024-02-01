package org.operation.app.lecture;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app/lectures")
public class LectureApiController implements AttendanceApi {
	//
	// private final LectureService lectureService;
	//
	// @ApiOperation(value = "진행 중인 세미나 상태 조회")
	// @GetMapping
	// public ResponseEntity<ResponseDTO> getLecture(@ApiIgnore Principal principal) {
	// 	val memberPlaygroundId = Long.parseLong(principal.getName());
	// 	val response = lectureService.getTodayLecture(memberPlaygroundId);
	// 	return ResponseEntity.ok(ResponseDTO.success(ResponseMessage.SUCCESS_SINGLE_GET_LECTURE.getMessage(), response));
	// }
	//
	// @ApiOperation(value = "출석 차수 조회")
	// @GetMapping("/round/{lectureId}")
	// public ResponseEntity<ResponseDTO> getRound(@PathVariable("lectureId") Long lectureId) {
	// 	val response = lectureService.getCurrentLectureRound(lectureId);
	// 	return ResponseEntity.ok(ResponseDTO.success(ResponseMessage.SUCCESS_GET_LECTURE_ROUND.getMessage(), response));
	// }
}
