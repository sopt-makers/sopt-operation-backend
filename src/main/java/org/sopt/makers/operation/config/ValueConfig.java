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
	@Value("${sopt.current.generation}")
	private int GENERATION;

	private final int SUB_LECTURE_MAX_ROUND = 2;
	private final String ETC_MESSAGE = "출석 점수가 반영되지 않아요.";
	private final String SEMINAR_MESSAGE = "";
	private final String EVENT_MESSAGE = "행사도 참여하고, 출석점수도 받고, 일석이조!";
}
