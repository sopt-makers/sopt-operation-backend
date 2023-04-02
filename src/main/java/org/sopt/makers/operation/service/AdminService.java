package org.sopt.makers.operation.service;

import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.dto.admin.LoginRequestDTO;
import org.sopt.makers.operation.dto.admin.LoginResponseDTO;
import org.sopt.makers.operation.dto.admin.SignUpRequestDTO;
import org.sopt.makers.operation.dto.admin.SignUpResponseDTO;
import org.sopt.makers.operation.entity.Admin;
import org.sopt.makers.operation.entity.AdminStatus;
import org.sopt.makers.operation.exception.AdminFailureException;
import org.sopt.makers.operation.repository.AdminRepository;
import org.sopt.makers.operation.security.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminService implements AdminServiceImpl {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO SignUpRequestDTO){
        isEmailDuplicated(SignUpRequestDTO.email());

        Admin admin = adminRepository.save(Admin.builder()
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
        Admin admin = adminRepository
                .findByEmail(userLoginRequestDTO.email())
                .orElseThrow(() -> new AdminFailureException("이메일이 존재하지 않습니다"));

        if(!passwordEncoder.matches(userLoginRequestDTO.password(), admin.getPassword())){
            throw new AdminFailureException("비밀번호가 일치하지 않습니다");
        }

        if(admin.getStatus().equals(AdminStatus.NOT_CERTIFIED)) throw new AdminFailureException("승인되지 않은 계정입니다");

        admin.updateRefreshToken(jwtTokenProvider.generateRefreshToken(admin));

        return new LoginResponseDTO(admin.getId(), admin.getName(), jwtTokenProvider.generateAccessToken(admin));
    }

    private void isEmailDuplicated(String email){
        if(adminRepository.existsByEmail(email)) throw new AdminFailureException("중복되는 이메일입니다");
    }

}
