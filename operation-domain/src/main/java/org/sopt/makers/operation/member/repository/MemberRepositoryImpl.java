package org.sopt.makers.operation.member.repository;

import static java.util.Objects.*;
import static org.sopt.makers.operation.common.domain.Part.*;
import static org.sopt.makers.operation.member.domain.QMember.*;

import java.util.List;

import org.sopt.makers.operation.common.domain.Part;
import org.sopt.makers.operation.member.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public int count(int generation, Part part) {
		return Math.toIntExact(queryFactory
			.select(member.count())
			.from(member)
			.where(
				member.generation.eq(generation),
				partEq(part)
			)
			.fetchFirst());
	}

	@Override
	public List<Member> find(int generation, Part part) {
		return queryFactory
				.selectFrom(member)
				.where(
						member.generation.eq(generation),
						partEq(part))
				.fetch();
	}

	private BooleanExpression partEq(Part part) {
		return (isNull(part) || part.equals(ALL)) ? null : member.part.eq(part);
	}

	@Override
	public List<Member> find(int generation, Part part, Pageable pageable) {
		StringExpression firstName = Expressions.stringTemplate("SUBSTR({0}, 1, 1)", member.name);
		return queryFactory
				.selectFrom(member)
				.where(
						partEq(part),
						member.generation.eq(generation)
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(firstName.asc())
				.fetch();
	}
}
