package org.sopt.makers.operation.controller;

import org.sopt.makers.operation.service.LectureService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleController {

	private final LectureService lectureService;

	@Scheduled(cron = "0 0 0 ? * SUN")
	public void endLecture() {
		lectureService.finishLecture();
	}

}
