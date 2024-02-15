package org.sopt.makers.operation.web.member.service;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.member.repository.MemberRepository;
import org.sopt.makers.operation.web.member.dto.response.MemberListGetResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebMemberServiceImpl implements WebMemberService {

    private final MemberRepository memberRepository;

    @Override
    public MemberListGetResponse getMembers(Part part, int generation, Pageable pageable) {
        val members = memberRepository.find(generation, part, pageable);
        val totalCount = memberRepository.count(generation, part);
        return MemberListGetResponse.of(members, totalCount);
    }
}
