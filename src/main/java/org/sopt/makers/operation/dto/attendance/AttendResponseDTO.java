package org.sopt.makers.operation.dto.attendance;

public record AttendResponseDTO(
        Long subLectureId
) {
    public static AttendResponseDTO of(Long subLectureId){
        return new AttendResponseDTO(
                subLectureId
        );
    }
}
