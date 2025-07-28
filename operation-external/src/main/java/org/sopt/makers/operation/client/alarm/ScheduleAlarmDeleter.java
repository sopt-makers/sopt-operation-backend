package org.sopt.makers.operation.client.alarm;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.springframework.stereotype.Component;

import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.code.failure.AlarmFailureCode;
import org.sopt.makers.operation.exception.AlarmException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.DeleteScheduleRequest;

import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.sopt.makers.operation.constant.AlarmConstant.ALARM_REQUEST_DATE_FORMAT;
import static org.sopt.makers.operation.constant.AlarmConstant.ALARM_REQUEST_SCHEDULE_TIME_FORMAT;

@Component
@RequiredArgsConstructor
public class ScheduleAlarmDeleter {

    private final ValueConfig valueConfig;
    private SchedulerClient schedulerClient;

    @PostConstruct
    private void init() {
        val awsCredentials = AwsBasicCredentials.create(valueConfig.getAccessKey(), valueConfig.getSecretKey());
        this.schedulerClient = SchedulerClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    public void deleteAlarm(long alarmId, LocalDateTime scheduleDateTime) {
        try {
            val eventName = generateEventName(alarmId, scheduleDateTime);
            schedulerClient.deleteSchedule(DeleteScheduleRequest.builder()
                    .name(eventName)
                    .build());
        } catch (RuntimeException e) {
            throw new AlarmException(AlarmFailureCode.FAIL_DELETE_SCHEDULE_ALARM);
        }
    }

    private String generateEventName(long alarmId, LocalDateTime scheduleDateTime) {
        val dateData = scheduleDateTime.toLocalDate().format(DateTimeFormatter.ofPattern(ALARM_REQUEST_DATE_FORMAT));
        val timeData = scheduleDateTime.toLocalTime().format(DateTimeFormatter.ofPattern(ALARM_REQUEST_SCHEDULE_TIME_FORMAT));
        return String.format("%s_%s_%d", dateData, timeData, alarmId);
    }
}
