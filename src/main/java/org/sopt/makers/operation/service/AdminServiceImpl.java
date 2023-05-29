package org.sopt.makers.operation.service;

import static org.sopt.makers.operation.common.ExceptionMessage.*;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.dto.admin.*;
import org.sopt.makers.operation.entity.Admin;
import org.sopt.makers.operation.entity.AdminStatus;
import org.sopt.makers.operation.exception.AdminFailureException;
import org.sopt.makers.operation.repository.AdminRepository;
import org.sopt.makers.operation.security.jwt.AdminAuthentication;
import org.sopt.makers.operation.security.jwt.JwtTokenProvider;
import org.sopt.makers.operation.security.jwt.JwtTokenType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminServiceImpl implements AdminService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO SignUpRequestDTO){
        isEmailDuplicated(SignUpRequestDTO.email());

        val admin = adminRepository.save(Admin.builder()
                .email(SignUpRequestDTO.email())
                .password(passwordEncoder.encode(SignUpRequestDTO.password()))
                .name(SignUpRequestDTO.name())
                .role(SignUpRequestDTO.role())
                .build());

        return new SignUpResponseDTO(admin.getId(), admin.getEmail(), admin.getName(), admin.getRole());
    }

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO userLoginRequestDTO) {
        val admin = adminRepository
                .findByEmail(userLoginRequestDTO.email())
                .orElseThrow(() -> new AdminFailureException("이메일이 존재하지 않습니다"));

        if(!passwordEncoder.matches(userLoginRequestDTO.password(), admin.getPassword())){
            throw new AdminFailureException("비밀번호가 일치하지 않습니다");
        }

        if(admin.getStatus().equals(AdminStatus.NOT_CERTIFIED)) throw new AdminFailureException("승인되지 않은 계정입니다");

        val authentication = new AdminAuthentication(admin.getId(), null, null);

        admin.updateRefreshToken(jwtTokenProvider.generateRefreshToken(authentication));

        return LoginResponseDTO.of(admin, jwtTokenProvider.generateAccessToken(authentication), admin.getRefreshToken());
    }

    @Override
    public void validateRefreshToken(Long adminId, String requestRefreshToken) {
        val admin = this.findById(adminId);
        val refreshToken = admin.getRefreshToken();

        if(!refreshToken.equals(requestRefreshToken)) throw new AdminFailureException("토큰이 일치하지 않습니다");
    }

    @Override
    @Transactional
    public RefreshResponseDTO refresh(String refreshToken) {
        val adminId = jwtTokenProvider.getId(refreshToken, JwtTokenType.REFRESH_TOKEN);

        validateRefreshToken(adminId, refreshToken);

        val adminAuthentication = new AdminAuthentication(adminId, null, null);
        val newAccessToken = jwtTokenProvider.generateAccessToken(adminAuthentication);
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(adminAuthentication);
        val admin = findById(adminId);

        admin.updateRefreshToken(newRefreshToken);

        return RefreshResponseDTO.of(newAccessToken, newRefreshToken);
    }

    private Admin findById(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminFailureException(INVALID_MEMBER.getName()));
    }

    private void isEmailDuplicated(String email) {
        if(adminRepository.existsByEmail(email)) throw new AdminFailureException("중복되는 이메일입니다");
    }
}
