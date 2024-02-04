package org.operation.web.alarm.api;

import org.operation.alarm.domain.Status;
import org.operation.common.domain.Part;
import org.operation.common.dto.BaseResponse;
import org.operation.web.alarm.dto.request.AlarmRequest;
import org.operation.web.alarm.dto.request.AlarmSendRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface AlarmApi {

	@Operation(
			summary = "알림 전송 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "알림 전송 성공"
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
	ResponseEntity<BaseResponse<?>> sendAlarm(@RequestBody AlarmSendRequest request);

	@Operation(
			summary = "알림 생성 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "알림 생성 성공"
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
	ResponseEntity<BaseResponse<?>> createAlarm(@RequestBody AlarmRequest request);

	@Operation(
			summary = "알림 리스트 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "알림 리스트 조회 성공"
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
	ResponseEntity<BaseResponse<?>> getAlarms(
			@RequestParam(required = false) Integer generation,
			@RequestParam(required = false) Part part,
			@RequestParam(required = false) Status status,
			Pageable pageable
	);

	@Operation(
			summary = "알림 상세 조회 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "알림 상세 조회 성공"
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
	ResponseEntity<BaseResponse<?>> getAlarm(@PathVariable long alarmId);

	@Operation(
			summary = "알림 삭제 API",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "알림 삭제 성공"
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
	ResponseEntity<BaseResponse<?>> deleteAlarm(@PathVariable long alarmId);
}
