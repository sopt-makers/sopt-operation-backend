package org.sopt.makers.operation.client.alarm;

import static org.sopt.makers.operation.constant.AlarmConstant.ALARM_REQUEST_DATE_FORMAT;
import static org.sopt.makers.operation.constant.AlarmConstant.ALARM_REQUEST_SCHEDULE_TIME_FORMAT;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.sopt.makers.operation.alarm.domain.AlarmLinkType;
import org.sopt.makers.operation.client.alarm.dto.AlarmRequest;
import org.sopt.makers.operation.client.alarm.dto.ScheduleAlarmRequest;
import org.sopt.makers.operation.client.alarm.dto.eventbridge.AlarmScheduleEventBridgeBody;
import org.sopt.makers.operation.client.alarm.dto.eventbridge.AlarmScheduleEventBridgeHeader;
import org.sopt.makers.operation.client.alarm.dto.eventbridge.AlarmScheduleEventBridgeRequest;
import org.sopt.makers.operation.code.failure.AlarmFailureCode;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.AlarmException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.val;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindow;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindowMode;
import software.amazon.awssdk.services.scheduler.model.Target;

@Component
@RequiredArgsConstructor
class ScheduleAlarmSender implements AlarmSender{
    private final ValueConfig valueConfig;
    private final SchedulerClient schedulerClient;
    private final ObjectMapper objectMapper;

    @Override
    public void sendAlarm(AlarmRequest alarmRequest) {
        ScheduleAlarmRequest scheduleRequest = (ScheduleAlarmRequest) alarmRequest;
        try {
            val name = generateEventName(scheduleRequest);
            val cronExpression = generateScheduleCronExpression(scheduleRequest);
            val eventJson = generateEventJson(scheduleRequest);
            val target = generateEventTarget(eventJson);

            val createScheduleRequest = generateEvent(name, target, cronExpression);
            schedulerClient.createSchedule(createScheduleRequest);
        } catch (Exception e) {
            throw new AlarmException(AlarmFailureCode.FAIL_SCHEDULE_ALARM);
        }
    }

    private CreateScheduleRequest generateEvent(String eventName, Target eventTarget, String eventCronExpression) {
        return CreateScheduleRequest.builder()
                .name(eventName)
                .target(eventTarget)
                .scheduleExpression(eventCronExpression)
                .actionAfterCompletion("DELETE")
                .flexibleTimeWindow(FlexibleTimeWindow.builder()
                        .mode(FlexibleTimeWindowMode.OFF)
                        .build())
                .build();
    }

    private Target generateEventTarget(String eventJson) {
        return Target.builder()
                .roleArn(valueConfig.getEventBridgeRoleArn())
                .arn(valueConfig.getNotificationLambdaArn())
                .input(eventJson)
                .build();
    }

    private String generateEventName(ScheduleAlarmRequest request) {
        val dateData = request.scheduleDateTime().toLocalDate().format(DateTimeFormatter.ofPattern(ALARM_REQUEST_DATE_FORMAT));
        val timeData = request.scheduleDateTime().toLocalTime().format(DateTimeFormatter.ofPattern(ALARM_REQUEST_SCHEDULE_TIME_FORMAT));
        return String.format("%s_%s_%d", dateData, timeData, request.alarmId());
    }

    private String generateScheduleCronExpression(ScheduleAlarmRequest request) {
        try {
            val localDateTime = request.scheduleDateTime();
            val utcDateTime = localDateTime.atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC"));

            return String.format("cron(%d %d %d %d ? %d)",
                    utcDateTime.getMinute(),
                    utcDateTime.getHour(),
                    utcDateTime.getDayOfMonth(),
                    utcDateTime.getMonthValue(),
                    utcDateTime.getYear());
        } catch (Exception e) {
            throw new AlarmException(AlarmFailureCode.INVALID_SCHEDULE_ALARM_FORMAT);
        }
    }

    private String generateEventJson(ScheduleAlarmRequest request) throws JsonProcessingException {
        val eventBody = convertToEventBridgeBody(request);
        val eventHeader = convertToEventBridgeHeader(request);

        val eventBridgeRequest = AlarmScheduleEventBridgeRequest.of(eventHeader, eventBody);
        return String.format("{\"detail\": %s}", objectMapper.writeValueAsString(eventBridgeRequest));
    }

    private AlarmScheduleEventBridgeHeader convertToEventBridgeHeader(ScheduleAlarmRequest request) {
        return AlarmScheduleEventBridgeHeader.builder()
                .alarmId(request.alarmId())
                .action(request.targetType().getAction().getValue())
                .xApiKey(valueConfig.getNOTIFICATION_KEY())
                .transactionId(UUID.randomUUID().toString())
                .service(valueConfig.getNOTIFICATION_HEADER_SERVICE())
                .build();
    }

    private AlarmScheduleEventBridgeBody convertToEventBridgeBody(ScheduleAlarmRequest request) {
        val deepLink = request.linkType().equals(AlarmLinkType.APP) ? request.link() : null;
        val webLink = request.linkType().equals(AlarmLinkType.WEB) ? request.link() : null;
        return AlarmScheduleEventBridgeBody.builder()
                .userIds(request.targets())
                .title(request.title())
                .content(request.content())
                .category(request.category())
                .deepLink(deepLink)
                .webLink(webLink)
                .build();
    }
}
