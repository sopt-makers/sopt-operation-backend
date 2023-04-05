package org.sopt.makers.operation.repository.member;

import java.util.List;

import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Member;

public interface MemberCustomRepository {
	List<Member> search(MemberSearchCondition condition);
}
