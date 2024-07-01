package org.sopt.makers.operation.client.alarm.eventBridgeServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.client.alarm.eventBridgeServer.dto.EventBridgeSenderRequest;
import org.sopt.makers.operation.code.failure.AlarmFailureCode;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.AlarmException;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindow;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindowMode;
import software.amazon.awssdk.services.scheduler.model.Target;

@Component
@RequiredArgsConstructor
public class EventBridgeSenderImpl implements EventBridgeSender {

    private SchedulerClient schedulerClient;
    private final ValueConfig valueConfig;
    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(valueConfig.getAccessKey(),
                valueConfig.getSecretKey());
        this.schedulerClient = SchedulerClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public void scheduleAlarm(EventBridgeSenderRequest request, String postDate, String postTime, Long alarmId) {
        try {
            String eventJson = String.format("{\"detail\": %s}", objectMapper.writeValueAsString(request));
            String formattedDate = formatCronExpression(postDate, postTime);
            String formattedTime = postTime.replace(":", "-");
            String scheduleName = String.format("%s_%s_%d", postDate, formattedTime, alarmId);

            Target target = Target.builder()
                    .roleArn(valueConfig.getEventBridgeRoleArn())
                    .arn(valueConfig.getNotificationLambdaArn())
                    .input(eventJson)
                    .build();

            CreateScheduleRequest createScheduleRequest = CreateScheduleRequest.builder()
                    .name(scheduleName)
                    .scheduleExpression(formattedDate)
                    .target(target)
                    .actionAfterCompletion("DELETE")
                    .flexibleTimeWindow(FlexibleTimeWindow.builder()
                            .mode(FlexibleTimeWindowMode.OFF)
                            .build())
                    .build();

            schedulerClient.createSchedule(createScheduleRequest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AlarmException(AlarmFailureCode.FAIL_SCHEDULE_ALARM);
        }
    }

    public static String formatCronExpression(String postDate, String postTime) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(postDate + " " + postTime, dateFormatter);

            ZonedDateTime utcDateTime = localDateTime.atZone(ZoneId.systemDefault())
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
}

