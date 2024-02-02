package org.operation.common.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class ValueConfig {

	@Value("${sopt.alarm.message.title_end}")
	private String ALARM_MESSAGE_TITLE;
	@Value("${sopt.alarm.message.content_end}")
	private String ALARM_MESSAGE_CONTENT;
	@Value("${sopt.current.generation}")
	private int GENERATION;
	@Value("${admin.url.prod}")
	private String ADMIN_PROD_URL;
	@Value("${admin.url.dev}")
	private String ADMIN_DEV_URL;
	@Value("${admin.url.local}")
	private String ADMIN_LOCAL_URL;
	@Value("${notification.key}")
	private String NOTIFICATION_KEY;
	@Value("${notification.url}")
	private String NOTIFICATION_URL;

	private final int SUB_LECTURE_MAX_ROUND = 2;
	private final String ETC_MESSAGE = "출석 점수가 반영되지 않아요.";
	private final String SEMINAR_MESSAGE = "";
	private final String EVENT_MESSAGE = "행사도 참여하고, 출석점수도 받고, 일석이조!";
	private final String SWAGGER_URI = "/swagger-ui/**";
	private final int ATTENDANCE_MINUTE = 10;

	private final List<String> APP_LINK_LIST = Arrays.asList(
			"home",
			"home/notification",
			"home/mypage",
			"home/attendance",
			"home/attendance/attendance-modal",
			"home/soptamp",
			"home/soptamp/entire-ranking",
			"home/soptamp/current-generation-ranking"
	);
	private final List<String> WEB_LINK_LIST = Arrays.asList(
			"https://playground.sopt.org/members",
			"https://playground.sopt.org/group"
	);
}
