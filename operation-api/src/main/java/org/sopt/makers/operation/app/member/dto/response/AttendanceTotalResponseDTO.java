package org.sopt.makers.operation.app.member.dto.response;

import java.util.List;

import org.sopt.makers.operation.app.attendance.dto.response.AttendanceTotalVO;
import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.member.domain.Member;

public record AttendanceTotalResponseDTO(
        Part part,
        int generation,
        String name,
        float score,
        AttendanceStatusListVO total,
        List<AttendanceTotalVO> attendances
) {
    public static AttendanceTotalResponseDTO of(Member member, List<AttendanceTotalVO> attendances){
        return new AttendanceTotalResponseDTO(
                member.getPart(),
                member.getGeneration(),
                member.getName(),
                member.getScore(),
                getTotal(member),
                attendances
        );
    }

    private static AttendanceStatusListVO getTotal(Member member) {
        return AttendanceStatusListVO.of(member);
    }
}
