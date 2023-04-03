package org.sopt.makers.operation.repository.member;

import static java.util.Objects.*;
import static org.sopt.makers.operation.entity.QMember.*;

import java.util.List;

import org.sopt.makers.operation.dto.member.MemberSearchCondition;
import org.sopt.makers.operation.entity.Member;
import org.sopt.makers.operation.entity.Part;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Member> search(MemberSearchCondition condition) {
		return queryFactory
			.selectFrom(member)
			.where(
				partEq(condition.part()),
				member.generation.eq(condition.generation())
			)
			.fetch();
	}

	private BooleanExpression partEq(Part part) {
		return nonNull(part) ? member.part.eq(part) : null;
	}
}
