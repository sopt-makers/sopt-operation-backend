package org.operation.member.repository;

import static java.util.Objects.*;
import static org.operation.attendance.QAttendance.*;
import static org.operation.lecture.QLecture.*;
import static org.operation.member.QMember.*;
import static org.sopt.makers.operation.entity.Part.*;

import java.util.List;
import java.util.Optional;

import org.operation.member.domain.Member;
import org.sopt.makers.operation.dto.member.response.MemberSearchCondition;
import org.sopt.makers.operation.entity.Part;
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
	public List<Member> search(MemberSearchCondition condition, Pageable pageable) {
		StringExpression firstName = Expressions.stringTemplate("SUBSTR({0}, 1, 1)", member.name);

		return queryFactory
			.selectFrom(member)
			.where(
				partEq(condition.part()),
				member.generation.eq(condition.generation())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
        	.orderBy(firstName.asc())
			.fetch();
	}

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

	@Override
	public Optional<Member> find(Long memberId) {
		return queryFactory
			.selectFrom(member)
			.join(member.attendances, attendance).fetchJoin().distinct()
			.join(attendance.lecture, lecture).fetchJoin()
			.where(member.id.eq(memberId))
			.stream().findFirst();
	}

	@Override
	public int countByGenerationAndPart(int generation, Part part) {
		return Math.toIntExact(queryFactory
			.select(member.count())
			.from(member)
			.where(
				member.generation.eq(generation),
				(nonNull(part) && !part.equals(ALL)) ? member.part.eq(part) : null
			)
			.fetchFirst());
	}

	@Override
	public List<Member> find(int generation, Part part) {
		StringExpression firstName = Expressions.stringTemplate("SUBSTR({0}, 1, 1)", member.name);

		return queryFactory
				.selectFrom(member)
				.where(
						member.generation.eq(generation),
						partEq(part))
				.orderBy(firstName.asc())
				.fetch();
	}

	private BooleanExpression partEq(Part part) {
		return (isNull(part) || part.equals(ALL)) ? null : member.part.eq(part);
	}
}
