package org.sopt.makers.operation.web.member.service;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.member.repository.MemberRepository;
import org.sopt.makers.operation.web.member.dto.response.MemberListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public MemberListResponse getMembers(Part part, int generation, Pageable pageable) {
        val memberList = memberRepository.find(generation, part, pageable);
        val totalCount = memberRepository.count(generation, part);
        return MemberListResponse.of(memberList, totalCount);
    }
}
