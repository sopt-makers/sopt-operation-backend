package org.sopt.makers.operation.app.member.dto.response;

import java.util.List;

import lombok.Builder;
import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.member.domain.Member;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record AttendanceTotalResponseDTO(
        Part part,
        int generation,
        String name,
        float score,
        AttendanceStatusListVO total,
        List<AttendanceTotalVO> attendances
) {
    public static AttendanceTotalResponseDTO of(Member member, List<AttendanceTotalVO> attendances){
        return AttendanceTotalResponseDTO.builder()
                .part(member.getPart())
                .generation(member.getGeneration())
                .name(member.getName())
                .score(member.getScore())
                .total(getTotal(member))
                .attendances(attendances)
                .build();
    }

    private static AttendanceStatusListVO getTotal(Member member) {
        return AttendanceStatusListVO.of(member);
    }
}
