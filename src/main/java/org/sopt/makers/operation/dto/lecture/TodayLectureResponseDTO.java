package org.sopt.makers.operation.dto.lecture;

import lombok.Builder;
import org.sopt.makers.operation.entity.attendance.AttendanceStatus;
import org.sopt.makers.operation.entity.attendance.SubAttendance;
import org.sopt.makers.operation.entity.lecture.Lecture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record TodayLectureResponseDTO(
        LectureResponseType type,
        Long id,
        String location,
        String name,
        String startDate,
        String endDate,
        String message,
        List<LectureGetResponseVO> attendances
) {
    public static TodayLectureResponseDTO of(LectureResponseType type, Lecture lecture, String message, List<SubAttendance> attendances) {

        return TodayLectureResponseDTO.builder()
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

    private static DateTimeFormatter convertFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
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
