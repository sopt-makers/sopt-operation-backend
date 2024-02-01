package org.sopt.makers.operation.service.batch;

import static org.sopt.makers.operation.common.ExceptionMessage.*;
import static org.operation.alarm.Attribute.*;

import java.util.*;

import lombok.val;

import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.dto.alarm.request.AlarmSenderDTO;

import org.operation.lecture.Lecture;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.external.alarm.AlarmSender;
import org.sopt.makers.operation.repository.lecture.LectureRepository;
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
				.orElseThrow(() -> new LectureException(INVALID_LECTURE.getName()));
	}

	private void checkEndLectureValidity(Lecture lecture) {
		if (!lecture.isEnd()) {
			throw new LectureException(NOT_END_TIME_YET.getName());
		}
	}

	private void sendAlarm(Lecture lecture) {
		val alarmTitle = getAlarmTitle(lecture);
		val alarmContent = valueConfig.getALARM_MESSAGE_CONTENT();
		val memberPlaygroundIds = getMemberPlaygroundIds(lecture);
		alarmSender.send(new AlarmSenderDTO(alarmTitle, alarmContent, memberPlaygroundIds, NEWS, null));
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
