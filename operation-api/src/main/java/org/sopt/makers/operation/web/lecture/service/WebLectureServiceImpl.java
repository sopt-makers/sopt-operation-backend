package org.sopt.makers.operation.web.lecture.service;

import static org.sopt.makers.operation.code.failure.LectureFailureCode.END_LECTURE;
import static org.sopt.makers.operation.code.failure.LectureFailureCode.INVALID_LECTURE;
import static org.sopt.makers.operation.code.failure.LectureFailureCode.NOT_END_TIME_YET;
import static org.sopt.makers.operation.code.failure.LectureFailureCode.NOT_STARTED_PRE_ATTENDANCE;
import static org.sopt.makers.operation.code.failure.LectureFailureCode.NO_SUB_LECTURE_EQUAL_ROUND;

import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.attendance.domain.SubAttendance;
import org.sopt.makers.operation.attendance.repository.attendance.AttendanceRepository;
import org.sopt.makers.operation.attendance.repository.subAttendance.SubAttendanceRepository;
import org.sopt.makers.operation.client.alarm.AlarmManager;
import org.sopt.makers.operation.client.alarm.dto.InstantAlarmRequest;
import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.LectureException;
import org.sopt.makers.operation.exception.SubLectureException;
import org.sopt.makers.operation.lecture.domain.Lecture;
import org.sopt.makers.operation.lecture.domain.SubLecture;
import org.sopt.makers.operation.lecture.repository.lecture.LectureRepository;
import org.sopt.makers.operation.lecture.repository.subLecture.SubLectureRepository;
import org.sopt.makers.operation.member.domain.Member;
import org.sopt.makers.operation.member.repository.MemberRepository;
import org.sopt.makers.operation.web.lecture.dto.request.LectureCreateRequest;
import org.sopt.makers.operation.web.lecture.dto.request.SubLectureStartRequest;
import org.sopt.makers.operation.web.lecture.dto.response.LectureCreateResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureDetailGetResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureGetResponse;
import org.sopt.makers.operation.web.lecture.dto.response.LectureListGetResponse;
import org.sopt.makers.operation.web.lecture.dto.response.SubLectureStartResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebLectureServiceImpl implements WebLectureService {
    public static final String NULL = "null";
    public static final String WHITE_SPACE = " ";

    private final LectureRepository lectureRepository;
    private final SubLectureRepository subLectureRepository;
    private final AttendanceRepository attendanceRepository;
    private final SubAttendanceRepository subAttendanceRepository;
    private final MemberRepository memberRepository;

    private final AlarmManager alarmManager;
    private final ValueConfig valueConfig;

    @Override
    @Transactional
    public LectureCreateResponse createLecture(LectureCreateRequest request) {
        val savedLecture = saveLecture(request);
        createAttendances(request.generation(), request.part(), savedLecture);
        return LectureCreateResponse.of(savedLecture);
    }

    @Override
    public LectureListGetResponse getLectures(int generation, Part part) {
        val lectures = lectureRepository.find(generation, part);
        return LectureListGetResponse.of(generation, lectures);
    }

    @Override
    public LectureGetResponse getLecture(long lectureId) {
        val lecture = findLecture(lectureId);
        return LectureGetResponse.of(lecture);
    }

    @Override
    @Transactional
    public SubLectureStartResponse startSubLecture(SubLectureStartRequest request) {
        val lecture = getLectureToStartAttendance(request.lectureId(), request.round());
        val subLecture = getSubLectureToStartAttendance(lecture, request.round());
        subLecture.updateCode(request.code());
        return SubLectureStartResponse.of(lecture, subLecture);
    }

    @Override
    @Transactional
    public void endLecture(long lectureId) {
        val lecture = getLectureReadyToEnd(lectureId);
        lecture.updateToEnd();
        sendAlarm(lecture);
    }

    @Override
    @Transactional
    public void endLectures() {
        val lectures = lectureRepository.findLecturesReadyToEnd();
        lectures.forEach(lecture -> endLecture(lecture.getId()));
    }

    @Override
    @Transactional
    public void deleteLecture(long lectureId) {
        val lecture = getLectureToDelete(lectureId);
        deleteRelationship(lecture);
        lectureRepository.deleteById(lectureId);
    }

    @Override
    public LectureDetailGetResponse getLectureDetail(long lectureId) {
        val lecture = findLecture(lectureId);
        return LectureDetailGetResponse.of(lecture);
    }

    private Lecture saveLecture(LectureCreateRequest request) {
        val savedLecture = lectureRepository.save(request.toEntity());
        createSubLectures(savedLecture);
        return savedLecture;
    }

    private void createSubLectures(Lecture lecture) {
        val maxRound = valueConfig.getSUB_LECTURE_MAX_ROUND();
        Stream.iterate(1, i -> i + 1).limit(maxRound)
                .forEach(round -> saveSubLecture(lecture, round));
    }

    private void saveSubLecture(Lecture lecture, int round) {
        subLectureRepository.save(new SubLecture(lecture, round));
    }

    private void createAttendances(int generation, Part part, Lecture lecture) {
        val members = memberRepository.find(generation, part);
        members.forEach(member -> saveAttendance(member, lecture));
    }

    private void saveAttendance(Member member, Lecture lecture) {
        val savedAttendance = attendanceRepository.save(new Attendance(member, lecture));
        createSubAttendances(savedAttendance);
    }

    private void createSubAttendances(Attendance attendance) {
        val subLectures = attendance.getLecture().getSubLectures();
        subLectures.forEach(subLecture -> saveSubAttendance(attendance, subLecture));
    }

    private void saveSubAttendance(Attendance attendance, SubLecture subLecture) {
        subAttendanceRepository.save(new SubAttendance(attendance, subLecture));
    }

    private Lecture findLecture(long id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new LectureException(INVALID_LECTURE));
    }

    private Lecture getLectureToStartAttendance(long lectureId, int round) {
        val lecture = findLecture(lectureId);
        if (lecture.isEnd()) {
            throw new LectureException(END_LECTURE);
        } else if (round == 2 && lecture.isBefore()) {
            throw new LectureException(NOT_STARTED_PRE_ATTENDANCE);
        }
        return lecture;
    }

    private SubLecture getSubLectureToStartAttendance(Lecture lecture, int round) {
        return lecture.getSubLectures().stream()
                .filter(l -> l.getRound() == round)
                .findFirst()
                .orElseThrow(() -> new SubLectureException(NO_SUB_LECTURE_EQUAL_ROUND));
    }

    private Lecture getLectureReadyToEnd(long lectureId) {
        val lecture = findLecture(lectureId);
        if (lecture.isNotYetToEnd()) {
            throw new LectureException(NOT_END_TIME_YET);
        }
        if (lecture.isEnd()) {
            throw new LectureException(END_LECTURE);
        }
        return lecture;
    }

    private void sendAlarm(Lecture lecture) {
        val alarmMessageTitle = String.join(WHITE_SPACE, lecture.getName(), valueConfig.getALARM_MESSAGE_TITLE());
        val alarmMessageContent = valueConfig.getALARM_MESSAGE_CONTENT();
        val targets = lecture.getAttendances().stream()
                .map(attendance -> String.valueOf(attendance.getMember().getPlaygroundId()))
                .filter(id -> !id.equals(NULL))
                .toList();
        val alarmRequest = InstantAlarmRequest.of(alarmMessageTitle, alarmMessageContent, targets);
        alarmManager.sendInstant(alarmRequest);
    }

    private Lecture getLectureToDelete(long lectureId) {
        val lecture = findLecture(lectureId);
        if (lecture.isEnd()) {
            restoreAttendances(lecture.getAttendances());
        }
        return lecture;
    }

    private void restoreAttendances(List<Attendance> attendances) {
        attendances.forEach(Attendance::restoreMemberScore);
    }

    private void deleteRelationship(Lecture lecture) {
        subAttendanceRepository.deleteAllBySubLectureIn(lecture.getSubLectures());
        subLectureRepository.deleteAllByLecture(lecture);
        attendanceRepository.deleteByLecture(lecture);
    }

}
