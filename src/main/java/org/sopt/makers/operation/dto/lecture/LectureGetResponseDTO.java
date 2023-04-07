package org.sopt.makers.operation.dto.lecture;

import java.time.LocalDateTime;
import java.util.List;

public record LectureGetResponseDTO(
        int type,
        String location,
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<LectureGetResponseVO> attendances
) {
}
