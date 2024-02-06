package org.sopt.makers.operation.member.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.member.domain.Member;
import org.sopt.makers.operation.common.dto.MemberSearchCondition;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
	List<Member> search(MemberSearchCondition condition, Pageable pageable);
	List<Member> search(MemberSearchCondition condition);
	Optional<Member> find(Long memberId);
	int count(int generation, Part part);
	List<Member> findOrderByName(int generation, Part part);
	List<Member> find(int generation, Part part);
	List<Member> find(int generation, Part part, Pageable pageable);
}
