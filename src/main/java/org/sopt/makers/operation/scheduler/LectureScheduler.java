package org.sopt.makers.operation.scheduler;

import org.sopt.makers.operation.service.LectureService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class LectureScheduler {

	private final LectureService lectureService;

	@Scheduled(cron = "0 0 0 ? * SUN")
	public void endLecture() {
		lectureService.endLectures();
	}

}
