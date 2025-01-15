package org.sopt.makers.operation.web.alarm.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.sopt.makers.operation.alarm.domain.AlarmStatus;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmInstantSendRequest;
import org.sopt.makers.operation.web.alarm.dto.request.AlarmScheduleSendRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface AlarmApi {

    @Operation(
            summary = "알림 즉시 전송 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "알림 즉시 발송 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "알림 즉시 발송에 실패하였습니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "올바르지 않은 알림 타겟 타입입니다."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    ResponseEntity<BaseResponse<?>> sendInstantAlarm(@RequestBody AlarmInstantSendRequest request);

    @Operation(
            summary = "알림 예약 발송 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "알림 예약 발송 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "알림 예약 발송에 실패하였습니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "올바르지 않은 알림 타겟 타입입니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "알림 예약 시간 포맷이 맞지 않습니다."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    ResponseEntity<BaseResponse<?>> sendScheduleAlarm(@RequestBody AlarmScheduleSendRequest request);

    @Operation(
            summary = "알림 리스트 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "알림 리스트 조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    ResponseEntity<BaseResponse<?>> getAlarms(
            @RequestParam(required = false) Integer generation,
            @RequestParam(required = false) AlarmStatus status,
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
                            responseCode = "404",
                            description = "알림이 존재하지 않습니다."
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
                            responseCode = "404",
                            description = "알림이 존재하지 않습니다."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    ResponseEntity<BaseResponse<?>> deleteAlarm(@PathVariable long alarmId);

    @Operation(
            summary = "알림 상태 업데이트 API (Web Hook)",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "알림 상태 업데이트 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "알림이 존재하지 않습니다."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    ResponseEntity<BaseResponse<?>> updateAlarmStatus(@PathVariable long alarmId);
}
