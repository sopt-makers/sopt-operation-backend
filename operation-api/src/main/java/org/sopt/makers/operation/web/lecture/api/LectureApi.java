package org.sopt.makers.operation.web.lecture.api;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.common.dto.BaseResponse;
import org.sopt.makers.operation.web.lecture.dto.request.AttendanceRequest;
import org.sopt.makers.operation.web.lecture.dto.request.LectureRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface LectureApi {

	@Operation(
			summary = "세션 생성 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "세션 생성 성공"
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 요청"
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류"
					)
			}
	)
	ResponseEntity<BaseResponse<?>> createLecture(@RequestBody LectureRequest request);

	@Operation(
			summary = "세션 리스트 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "세션 리스트 조회 성공"
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 요청"
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류"
					)
			}
	)
	ResponseEntity<BaseResponse<?>> getLectures(
			@RequestParam int generation,
			@RequestParam(required = false) Part part);

	@Operation(
			summary = "세션 단일 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "세션 단일 조회 성공"
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 요청"
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류"
					)
			}
	)
	ResponseEntity<BaseResponse<?>> getLecture(@PathVariable long lectureId);

	@Operation(
			summary = "출석 시작 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "출석 시작 성공"
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 요청"
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류"
					)
			}
	)
	ResponseEntity<BaseResponse<?>> startAttendance(@RequestBody AttendanceRequest request);

	@Operation(
			summary = "세션 종료 후 출석 점수 갱신 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "세션 종료 후 출석 점수 갱신 성공"
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 요청"
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류"
					)
			}
	)
	ResponseEntity<BaseResponse<?>> endLecture(@PathVariable long lectureId);

	@Operation(
			summary = "세션 삭제 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "세션 삭제 성공"
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 요청"
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류"
					)
			}
	)
	ResponseEntity<BaseResponse<?>> deleteLecture(@PathVariable long lectureId);

	@Operation(
			summary = "세션 팝업용 상세 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "세션 팝업용 상세 조회 성공"
					),
					@ApiResponse(
							responseCode = "400",
							description = "잘못된 요청"
					),
					@ApiResponse(
							responseCode = "500",
							description = "서버 내부 오류"
					)
			}
	)
	ResponseEntity<BaseResponse<?>> getLectureDetail(@PathVariable long lectureId);
}
