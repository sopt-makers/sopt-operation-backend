package org.sopt.makers.operation.app.lecture.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.*;

import org.sopt.makers.operation.attendance.domain.AttendanceStatus;
import org.sopt.makers.operation.attendance.domain.SubAttendance;
import org.sopt.makers.operation.lecture.domain.Lecture;

import lombok.Builder;

@Builder(access = PRIVATE)
public record TodayLectureResponse(
        LectureResponseType type,
        long id,
        String location,
        String name,
        String startDate,
        String endDate,
        String message,
        List<LectureGetResponseVO> attendances
) {
    public static TodayLectureResponse of(LectureResponseType type, Lecture lecture, String message, List<SubAttendance> attendances) {
        return TodayLectureResponse.builder()
                .type(type)
                .id(lecture.getId())
                .location(lecture.getPlace())
                .name(lecture.getName())
                .startDate(lecture.getStartDate().format(convertFormat()))
                .endDate(lecture.getEndDate().format(convertFormat()))
                .message(message)
                .attendances(attendances.stream()
                        .map(subAttendance -> LectureGetResponseVO.of(subAttendance.getStatus(), subAttendance.getLastModifiedDate()))
                        .collect(Collectors.toList()))
                .build();
    }

    public static TodayLectureResponse getEmptyResponse() {
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

    private static DateTimeFormatter convertFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    }

    public static TodayLectureResponse getOnAttendanceLectureResponse(
            SubAttendance subAttendance,
            Lecture lecture,
            LectureResponseType responseType,
            String message
    ) {
        return lecture.isFirst()
                ? TodayLectureResponse.of(responseType, lecture, message, Collections.emptyList())
                : TodayLectureResponse.of(responseType, lecture, message, Collections.singletonList(subAttendance));
    }

    public static TodayLectureResponse getAttendanceLectureResponse(
            List<SubAttendance> subAttendances,
            SubAttendance subAttendance,
            Lecture lecture,
            LectureResponseType responseType,
            String message
    ) {
        return lecture.isFirst()
                ? TodayLectureResponse.of(responseType, lecture, message, Collections.singletonList(subAttendance))
                : TodayLectureResponse.of(responseType, lecture, message, subAttendances);
    }

    @Builder
    record LectureGetResponseVO(
            AttendanceStatus status,
            String attendedAt

    ) {
        public static LectureGetResponseVO of(AttendanceStatus status, LocalDateTime attendedAt) {
            return LectureGetResponseVO.builder()
                    .status(status)
                    .attendedAt(attendedAt.format((convertFormat())))
                    .build();
        }

        private static DateTimeFormatter convertFormat() {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        }
    }
}
