package org.sopt.makers.operation.repository.member;

import java.util.List;
import java.util.Optional;

import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Member;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
	List<Member> search(MemberSearchCondition condition, Pageable pageable);
	List<Member> search(MemberSearchCondition condition);
	Optional<Member> findMemberByIdFetchJoinAttendances(Long memberId);
}
