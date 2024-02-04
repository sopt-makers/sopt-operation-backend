package org.sopt.makers.operation.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.makers.operation.domain.Part;
import org.sopt.makers.operation.domain.member.domain.Member;
import org.sopt.makers.operation.dto.MemberSearchCondition;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
	List<Member> search(MemberSearchCondition condition, Pageable pageable);
	List<Member> search(MemberSearchCondition condition);
	Optional<Member> find(Long memberId);
	int countByGenerationAndPart(int generation, Part part);
	List<Member> findOrderByName(int generation, Part part);
	List<Member> find(int generation, Part part);
}
