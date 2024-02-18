package org.sopt.makers.operation.web.admin.service;

import static org.sopt.makers.operation.code.failure.member.memberFailureCode.*;
import static org.sopt.makers.operation.code.failure.admin.AdminFailureCode.*;

import org.sopt.makers.operation.admin.domain.Admin;
import org.sopt.makers.operation.authentication.AdminAuthentication;
import org.sopt.makers.operation.exception.AdminFailureException;
import org.sopt.makers.operation.jwt.JwtTokenProvider;
import org.sopt.makers.operation.jwt.JwtTokenType;
import org.sopt.makers.operation.web.admin.dto.request.SignUpRequest;
import org.sopt.makers.operation.web.admin.dto.response.SignUpResponse;
import org.sopt.makers.operation.web.admin.dto.request.LoginRequest;
import org.sopt.makers.operation.web.admin.dto.response.LoginResponse;
import org.sopt.makers.operation.web.admin.dto.response.RefreshResponse;
import org.sopt.makers.operation.admin.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final AdminRepository adminRepository;

	@Override
	@Transactional
	public SignUpResponse signUp(SignUpRequest request){
		checkEmailDuplicated(request.email());
		val entity = request.toEntity(passwordEncoder.encode(request.password()));
		val admin = adminRepository.save(entity);
		return SignUpResponse.of(admin);
	}

	private void checkEmailDuplicated(String email) {
		val isExist = adminRepository.existsByEmail(email);
		if (isExist) {
			throw new AdminFailureException(DUPLICATED_EMAIL);
		}
	}

	@Override
	@Transactional
	public LoginResponse login(LoginRequest request) {
		val admin = findByEmail(request.email());
		checkPasswordMatched(request.password(), admin);
		checkAdminAllowed(admin);
		val refreshToken = generateRefreshToken(admin);
		admin.updateRefreshToken(refreshToken);
		val accessToken = generateAccessToken(admin);
		return LoginResponse.of(admin, accessToken);
	}

	private String generateAccessToken(Admin admin) {
		val adminAuthentication = new AdminAuthentication(admin.getId(), null, null);
		return jwtTokenProvider.generateAccessToken(adminAuthentication);
	}

	private String generateRefreshToken(Admin admin) {
		val authentication = new AdminAuthentication(admin.getId(), null, null);
		return jwtTokenProvider.generateRefreshToken(authentication);
	}

	private Admin findByEmail(String email) {
		return adminRepository.findByEmail(email)
				.orElseThrow(() -> new AdminFailureException(INVALID_EMAIL));
	}

	private void checkPasswordMatched(String password, Admin admin) {
		if (!passwordEncoder.matches(password, admin.getPassword())) {
			throw new AdminFailureException(INVALID_PASSWORD);
		}
	}

	private void checkAdminAllowed(Admin admin) {
		if (admin.isNotAllowed()) {
			throw new AdminFailureException(NOT_APPROVED_ACCOUNT);
		}
	}

	@Override
	@Transactional
	public RefreshResponse refresh(String refreshToken) {
		val adminId = jwtTokenProvider.getId(refreshToken, JwtTokenType.REFRESH_TOKEN);
		val admin = findById(adminId);
		validateRefreshToken(admin, refreshToken);
		val newAccessToken = generateAccessToken(admin);

		return RefreshResponse.of(newAccessToken);
	}

	public void validateRefreshToken(Admin admin, String refreshToken) {
		if (!admin.isMatchRefreshToken(refreshToken)) {
			throw new AdminFailureException(INVALID_REFRESH_TOKEN);
		}
	}

	private Admin findById(Long adminId) {
		return adminRepository.findById(adminId)
				.orElseThrow(() -> new AdminFailureException(INVALID_MEMBER));
	}
}
