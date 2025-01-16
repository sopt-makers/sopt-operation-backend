package org.sopt.makers.operation.dummy;

import lombok.Builder;
import org.sopt.makers.operation.attendance.domain.Attendance;
import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.member.domain.Member;
import org.sopt.makers.operation.member.domain.ObYb;

import java.util.List;

public class MemberDummy extends Member {
    @Builder
    public MemberDummy(Long id, Long playgroundId, String name, int generation, ObYb obyb, Part part, String university, float score, String phone, List<Attendance> attendances) {
        super(id, playgroundId, name, generation, obyb, part, university, score, phone, attendances);
    }
}
