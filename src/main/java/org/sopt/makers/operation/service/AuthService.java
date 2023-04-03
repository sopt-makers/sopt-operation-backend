package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;

import javax.persistence.EntityNotFoundException;

import org.sopt.makers.operation.common.ExceptionMessage;
import org.sopt.makers.operation.repository.MemberRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;

	public void confirmAdmin(Long memberId) {
		memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_MEMBER.getName()));
	}
}
