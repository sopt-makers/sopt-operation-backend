package org.operation.web.attendnace.api;

import org.operation.common.domain.Part;
import org.operation.common.dto.BaseResponse;
import org.operation.web.attendance.dto.request.SubAttendanceUpdateRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface AttendanceApi {

	@Operation(
			summary = "출석 상태 변경 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "출석 상태 변경 성공"
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
	ResponseEntity<BaseResponse<?>> updateSubAttendance(@RequestBody SubAttendanceUpdateRequest request);

	@Operation(
			summary = "회원별 출석 정보 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "회원별 출석 정보 조회 성공"
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
	ResponseEntity<BaseResponse<?>> findAttendancesByMember(@PathVariable long memberId);

	@Operation(
			summary = "출석 점수 갱신 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "출석 점수 갱신 성공"
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
	ResponseEntity<BaseResponse<?>> updateMemberScore(@PathVariable long memberId);

	@Operation(
			summary = "세션별 출석 정보 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "세션별 출석 정보 조회 성공"
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
	ResponseEntity<BaseResponse<?>> findAttendancesByLecture(
			@PathVariable long lectureId,
			@RequestParam(required = false) Part part,
			Pageable pageable);
}
