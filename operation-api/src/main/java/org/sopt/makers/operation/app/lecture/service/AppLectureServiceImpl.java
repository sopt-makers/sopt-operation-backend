package org.sopt.makers.operation.app.lecture.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.sopt.makers.operation.attendance.domain.AttendanceStatus.*;
import static org.sopt.makers.operation.code.failure.lecture.LectureFailureCode.*;
import static org.sopt.makers.operation.code.failure.subLecture.subLectureFailureCode.*;
import static org.sopt.makers.operation.lecture.domain.Attribute.*;
import static org.sopt.makers.operation.lecture.domain.LectureStatus.*;

import org.sopt.makers.operation.app.lecture.dto.response.LectureCurrentRoundResponse;
import org.sopt.makers.operation.app.lecture.dto.response.LectureResponseType;
import org.sopt.makers.operation.app.lecture.dto.response.TodayLectureResponse;
import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.attendance.domain.SubAttendance;
import org.sopt.makers.operation.attendance.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.SubLectureException;
import org.sopt.makers.operation.lecture.domain.Attribute;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.domain.SubLecture;
import org.sopt.makers.operation.lecture.repository.lecture.LectureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppLectureServiceImpl implements AppLectureService {

    private final LectureRepository lectureRepository;
    private final AttendanceRepository attendanceRepository;
    private final ValueConfig valueConfig;

    @Override
    public TodayLectureResponse getTodayLecture(long memberPlaygroundId) {
        val attendances = attendanceRepository.findToday(memberPlaygroundId);
        checkAttendancesSize(attendances);

        if (attendances.isEmpty()) {
            return TodayLectureResponse.getEmptyResponse();
        }

        val attendance = getNowAttendance(attendances);
        val lecture = attendance.getLecture();
        val responseType = getResponseType(lecture);
        val message = getMessage(lecture.getAttribute());

        if (responseType.equals(LectureResponseType.NO_ATTENDANCE) || lecture.isBefore()) {
            return TodayLectureResponse.of(responseType, lecture, message, Collections.emptyList());
        }

        val subAttendances = attendance.getSubAttendances();

        return getTodayLectureResponse(subAttendances, responseType, lecture);
    }

    private void checkAttendancesSize(List<Attendance> attendances) {
        if (attendances.size() > valueConfig.getSUB_LECTURE_MAX_ROUND()) {
            throw new LectureException(INVALID_COUNT_SESSION);
        }
    }

    private boolean checkOnAttendanceAbsence(SubLecture subLecture, SubAttendance subAttendance) {
        val isOnAttendanceCheck = subLecture.isEnded(valueConfig.getATTENDANCE_MINUTE());
        return !isOnAttendanceCheck && subAttendance.getStatus().equals(ABSENT);
    }

    private Attendance getNowAttendance(List<Attendance> attendances) {
        val index = getAttendanceIndex(attendances);
        return attendances.get(index);
    }

    private int getAttendanceIndex(List<Attendance> attendances) {
        val isMultipleAttendance = getIsMultipleAttendance(attendances.size());
        return isMultipleAttendance ? 1 : 0;
    }
    private boolean getIsMultipleAttendance(int lectureCount) {
        return LocalDateTime.now().getHour() >= valueConfig.getHACKATHON_LECTURE_START_HOUR()
                && lectureCount == valueConfig.getMAX_LECTURE_COUNT();
    }

    private SubAttendance getNowSubAttendance(List<SubAttendance> subAttendances, Lecture lecture) {
        val index = getSubAttendanceIndex(lecture);
        return subAttendances.get(index);
    }

    private int getSubAttendanceIndex(Lecture lecture) {
        return lecture.isFirst() ? 0 : 1;
    }

    private LectureResponseType getResponseType(Lecture lecture) {
        val attribute = lecture.getAttribute();
        return attribute.equals(ETC) ? LectureResponseType.NO_ATTENDANCE : LectureResponseType.HAS_ATTENDANCE;
    }

    private String getMessage(Attribute attribute) {
        return switch (attribute) {
            case SEMINAR -> valueConfig.getSEMINAR_MESSAGE();
            case EVENT -> valueConfig.getEVENT_MESSAGE();
            case ETC -> valueConfig.getETC_MESSAGE();
        };
    }

    private TodayLectureResponse getTodayLectureResponse(
            List<SubAttendance> subAttendances,
            LectureResponseType responseType,
            Lecture lecture
    ) {
        val subAttendance = getNowSubAttendance(subAttendances, lecture);
        val subLecture = subAttendance.getSubLecture();
        val message = getMessage(lecture.getAttribute());

        if (checkOnAttendanceAbsence(subLecture, subAttendance)) {
            return TodayLectureResponse.getOnAttendanceLectureResponse(subAttendance, lecture, responseType, message);
        }

        return TodayLectureResponse.getAttendanceLectureResponse(subAttendances, subAttendance, lecture, responseType, message);
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
                .orElseThrow(() -> new LectureException(INVALID_LECTURE));
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
                .orElseThrow(() -> new SubLectureException(NO_SUB_LECTURE_EQUAL_ROUND));
    }

    private void checkLectureExist(Lecture lecture) {
        val today = LocalDate.now();
        val startOfDay = today.atStartOfDay();
        val endOfDay = LocalDateTime.of(today, LocalTime.MAX);
        val startAt = lecture.getStartDate();
        if (startAt.isBefore(startOfDay) || startAt.isAfter(endOfDay)) {
            throw new LectureException(NO_SESSION);
        }
    }

    private void checkLectureBefore(Lecture lecture) {
        if (lecture.isBefore()) {
            throw new LectureException(NOT_STARTED_ATTENDANCE);
        }
    }

    private void checkEndAttendance(SubLecture subLecture) {
        if (subLecture.isEnded(valueConfig.getATTENDANCE_MINUTE())) {
            throw new LectureException(ENDED_ATTENDANCE, subLecture.getRound());
        }
    }

    private void checkLectureEnd(Lecture lecture) {
        if (lecture.isEnd()) {
            throw new LectureException(END_LECTURE);
        }
    }
}
