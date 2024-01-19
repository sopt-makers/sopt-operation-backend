package org.sopt.makers.operation.config;

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

	private final int SUB_LECTURE_MAX_ROUND = 2;
}
