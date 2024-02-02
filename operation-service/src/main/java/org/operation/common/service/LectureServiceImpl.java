package org.operation.common.service;

import static org.operation.alarm.domain.Attribute.*;
import static org.operation.lecture.message.ErrorMessage.*;

import java.util.*;

import lombok.val;

import org.operation.client.alarm.AlarmSender;
import org.operation.common.config.ValueConfig;
import org.operation.common.exception.LectureException;
import org.operation.common.scheduler.service.LectureService;

import org.operation.lecture.Lecture;
import org.operation.lecture.repository.lecture.LectureRepository;
import org.operation.web.alarm.dto.request.AlarmSenderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureServiceImpl implements LectureService {

	private final LectureRepository lectureRepository;
	private final AlarmSender alarmSender;
	private final ValueConfig valueConfig;

	@Override
	@Transactional
	public void endLectures() {
		val lectures = lectureRepository.findLecturesToBeEnd();
		lectures.forEach(lecture -> endLecture(lecture.getId()));
	}

	private void endLecture(Long lectureId) {
		val lecture = findLecture(lectureId);
		checkEndLectureValidity(lecture);
		lecture.updateToEnd();
		sendAlarm(lecture);
	}

	private Lecture findLecture(Long id) {
		return lectureRepository.findById(id)
				.orElseThrow(() -> new LectureException(INVALID_LECTURE.getContent()));
	}

	private void checkEndLectureValidity(Lecture lecture) {
		if (!lecture.isEnd()) {
			throw new LectureException(NOT_END_TIME_YET.getContent());
		}
	}

	private void sendAlarm(Lecture lecture) {
		val alarmTitle = getAlarmTitle(lecture);
		val alarmContent = valueConfig.getALARM_MESSAGE_CONTENT();
		val memberPlaygroundIds = getMemberPlaygroundIds(lecture);
		alarmSender.send(new AlarmSenderRequest(alarmTitle, alarmContent, memberPlaygroundIds, NEWS, null));
	}

	private List<String> getMemberPlaygroundIds(Lecture lecture) {
		return lecture.getAttendances().stream()
				.map(attendance -> String.valueOf(attendance.getMember().getPlaygroundId()))
				.filter(id -> !id.equals("null"))
				.toList();
	}

	private String getAlarmTitle(Lecture lecture) {
		return lecture.getName() + " " + valueConfig.getALARM_MESSAGE_TITLE();
	}

}
