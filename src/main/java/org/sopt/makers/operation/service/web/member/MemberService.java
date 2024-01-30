package org.sopt.makers.operation.service.web.member;

import org.sopt.makers.operation.dto.member.response.MembersResponseDTO;
import org.sopt.makers.operation.entity.Part;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MembersResponseDTO getMemberList(Part part, int generation, Pageable pageable);
}
