package org.sopt.makers.operation.member.repository;

import java.util.List;

import org.sopt.makers.operation.member.domain.Part;
import org.sopt.makers.operation.member.domain.Member;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
	int count(int generation, Part part);
	List<Member> find(int generation, Part part);
	List<Member> find(int generation, Part part, Pageable pageable);
}
