package org.sopt.makers.operation.dto.member;

import org.sopt.makers.operation.dto.attendance.AttendanceTotalCountVO;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;

public record MemberListGetResponse(
        Long id,
        String name,
        String university,
        Part part,
        float score,
        AttendanceTotalCountVO total
)
{
    public static MemberListGetResponse of(Member member, AttendanceTotalCountVO total){
        return new MemberListGetResponse(
                member.getId(),
                member.getName(),
                member.getUniversity(),
                member.getPart(),
                member.getScore(),
                total
        );
    }
}