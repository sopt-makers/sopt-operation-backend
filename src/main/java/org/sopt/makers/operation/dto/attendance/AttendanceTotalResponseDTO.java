package org.sopt.makers.operation.dto.attendance;

import org.sopt.makers.operation.entity.member.Member;
import org.sopt.makers.operation.entity.Part;

import java.util.List;

public record AttendanceTotalResponseDTO(
        Part part,
        int generation,
        String name,
        float score,
        AttendanceTotalCountVO total,
        List<AttendanceTotalVO> attendances
)
{
    public static AttendanceTotalResponseDTO of(Member member, AttendanceTotalCountVO total, List<AttendanceTotalVO> attendances){
        return new AttendanceTotalResponseDTO(
                member.getPart(),
                member.getGeneration(),
                member.getName(),
                member.getScore(),
                total,
                attendances
        );
    }
}
