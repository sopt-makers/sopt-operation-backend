package org.sopt.makers.operation.repository.member;

import static java.util.Objects.*;
import static org.sopt.makers.operation.entity.QMember.*;

import java.util.List;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
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
		StringExpression firstName = Expressions.stringTemplate("SUBSTR({0}, 1, 1)", member.name);

		return queryFactory
			.selectFrom(member)
			.where(
				partEq(condition.part()),
				member.generation.eq(condition.generation())
			)
        	.orderBy(firstName.asc())
			.fetch();
	}

	private BooleanExpression partEq(Part part) {
		return nonNull(part) ? member.part.eq(part) : null;
	}
}
