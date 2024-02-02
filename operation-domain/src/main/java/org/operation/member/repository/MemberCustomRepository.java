package org.operation.member.repository;

import java.util.List;
import java.util.Optional;

import org.operation.common.domain.Part;
import org.operation.common.dto.MemberSearchCondition;
import org.operation.member.domain.Member;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
	List<Member> search(MemberSearchCondition condition, Pageable pageable);
	List<Member> search(MemberSearchCondition condition);
	Optional<Member> find(Long memberId);
	int countByGenerationAndPart(int generation, Part part);
	List<Member> find(int generation, Part part);
}
