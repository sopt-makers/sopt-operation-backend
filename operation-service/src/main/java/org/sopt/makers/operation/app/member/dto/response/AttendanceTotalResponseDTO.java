package org.sopt.makers.operation.app.member.dto.response;

import java.util.List;

import org.sopt.makers.operation.domain.Part;
import org.operation.member.domain.Member;
import org.sopt.makers.operation.web.member.dto.response.AttendanceTotalCountVO;

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
