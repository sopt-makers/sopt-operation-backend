package org.sopt.makers.operation.app.lecture.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.sopt.makers.operation.domain.attendance.domain.AttendanceStatus.*;
import static org.sopt.makers.operation.domain.lecture.LectureStatus.*;
import static org.sopt.makers.operation.code.failure.lecture.lectureFailureCode.*;
import static org.sopt.makers.operation.code.failure.subLecture.subLectureFailureCode.*;

import org.sopt.makers.operation.app.lecture.dto.response.LectureCurrentRoundResponse;
import org.sopt.makers.operation.app.lecture.dto.response.LectureResponseType;
import org.sopt.makers.operation.app.lecture.dto.response.TodayLectureResponse;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.domain.attendance.domain.SubAttendance;
import org.sopt.makers.operation.domain.attendance.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.domain.lecture.Attribute;
import org.sopt.makers.operation.domain.lecture.Lecture;
import org.sopt.makers.operation.domain.lecture.SubLecture;
import org.sopt.makers.operation.domain.attendance.domain.Attendance;
import org.sopt.makers.operation.domain.lecture.repository.lecture.LectureRepository;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.SubLectureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final AttendanceRepository attendanceRepository;
    private final ValueConfig valueConfig;

    @Override
    public TodayLectureResponse getTodayLecture(long memberPlaygroundId) {
        val attendances = attendanceRepository.findToday(memberPlaygroundId);
        checkAttendancesSize(attendances);

        if (attendances.isEmpty()) {
            return getEmptyResponse();
        }

        val attendance = getNowAttendance(attendances);
        val lecture = attendance.getLecture();
        val responseType = getResponseType(lecture);
        val message = getMessage(lecture.getAttribute());

        if (responseType.equals(LectureResponseType.NO_ATTENDANCE) || lecture.isBefore()) {
            return TodayLectureResponse.of(responseType, lecture, message, Collections.emptyList());
        }

        val subAttendances = attendance.getSubAttendances();
        val subAttendance = lecture.isFirst() ? subAttendances.get(0) : subAttendances.get(1);
        return getTodayLectureResponse(subAttendance, responseType, lecture);
    }

    private TodayLectureResponse getEmptyResponse() {
        return TodayLectureResponse.builder()
                .type(LectureResponseType.NO_SESSION)
                .id(0L)
                .location("")
                .name("")
                .startDate("")
                .endDate("")
                .message("")
                .attendances(Collections.emptyList())
                .build();
    }

    private void checkAttendancesSize(List<Attendance> attendances) {
        if (attendances.size() > valueConfig.getSUB_LECTURE_MAX_ROUND()) {
            throw new LectureException(INVALID_COUNT_SESSION.getMessage());
        }
    }

    private Attendance getNowAttendance(List<Attendance> attendances) {
        val index = getAttendanceIndex();
        return attendances.get(index);
    }

    private int getAttendanceIndex() {
        return (LocalDateTime.now().getHour() >= 16) ? 1 : 0;
    }

    private LectureResponseType getResponseType(Lecture lecture) {
        val attribute = lecture.getAttribute();
        return attribute.equals(Attribute.ETC) ? LectureResponseType.NO_ATTENDANCE : LectureResponseType.HAS_ATTENDANCE;
    }

    private String getMessage(Attribute attribute) {
        return switch (attribute) {
            case SEMINAR -> valueConfig.getSEMINAR_MESSAGE();
            case EVENT -> valueConfig.getEVENT_MESSAGE();
            case ETC -> valueConfig.getETC_MESSAGE();
        };
    }

    private TodayLectureResponse getTodayLectureResponse(SubAttendance subAttendance, LectureResponseType responseType, Lecture lecture) {
        val subLecture = subAttendance.getSubLecture();
        val isOnAttendanceCheck = LocalDateTime.now().isBefore(subLecture.getStartAt().plusMinutes(10));
        val message = getMessage(lecture.getAttribute());
        if (isOnAttendanceCheck && subAttendance.getStatus().equals(ABSENT)) {
            return TodayLectureResponse.of(responseType, lecture, message, Collections.emptyList());
        }
        return TodayLectureResponse.of(responseType, lecture, message, Collections.singletonList(subAttendance));
    }

    @Override
    public LectureCurrentRoundResponse getCurrentLectureRound(long lectureId) {
        val lecture = findLecture(lectureId);
        val subLecture = getSubLecture(lecture);
        checkLectureExist(lecture);
        checkLectureBefore(lecture);
        checkEndAttendance(subLecture);
        checkLectureEnd(lecture);
        return LectureCurrentRoundResponse.of(subLecture);
    }

    private Lecture findLecture(Long id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new LectureException(INVALID_LECTURE.getMessage()));
    }

    private SubLecture getSubLecture(Lecture lecture) {
        val status = lecture.getLectureStatus();
        val round = status.equals(FIRST) ? 1 : 2;
        return getSubLecture(lecture, round);
    }

    private SubLecture getSubLecture(Lecture lecture, int round) {
        return lecture.getSubLectures().stream()
                .filter(l -> l.getRound() == round)
                .findFirst()
                .orElseThrow(() -> new SubLectureException(NO_SUB_LECTURE_EQUAL_ROUND.getMessage()));
    }

    private void checkLectureExist(Lecture lecture) {
        val today = LocalDate.now();
        val startOfDay = today.atStartOfDay();
        val endOfDay = LocalDateTime.of(today, LocalTime.MAX);
        val startAt = lecture.getStartDate();
        if (startAt.isBefore(startOfDay) || startAt.isAfter(endOfDay)) {
            throw new LectureException(NO_SESSION.getMessage());
        }
    }

    private void checkLectureBefore(Lecture lecture) {
        if (lecture.isBefore()) {
            throw new LectureException(NOT_STARTED_ATTENDANCE.getMessage());
        }
    }

    private void checkEndAttendance(SubLecture subLecture) {
        if (isEndAttendance(subLecture)) {
            throw new LectureException(subLecture.getRound() + ENDED_ATTENDANCE.getMessage());
        }
    }

    private boolean isEndAttendance(SubLecture subLecture) {
        val status = subLecture.getLecture().getLectureStatus();
        if (LocalDateTime.now().isAfter(subLecture.getStartAt().plusMinutes(10))) {
            return status.equals(FIRST) || status.equals(SECOND);
        }
        return false;
    }

    private void checkLectureEnd(Lecture lecture) {
        if (lecture.isEnd()) {
            throw new LectureException(END_LECTURE.getMessage());
        }
    }
}