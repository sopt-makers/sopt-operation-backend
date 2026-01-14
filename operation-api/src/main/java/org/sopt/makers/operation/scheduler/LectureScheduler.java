package org.sopt.makers.operation.scheduler;

import org.sopt.makers.operation.web.lecture.service.WebLectureService;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Profile({"dev", "prod"})
public class LectureScheduler {

	private final WebLectureService lectureService;

	@Scheduled(cron = "0 0 0 ? * SUN")
	public void endLecture() {
		lectureService.endLectures();
	}

}
