package org.operation.web.admin.service;

import org.operation.admin.domain.Admin;
import org.operation.web.admin.dto.request.LoginRequest;
import org.operation.web.admin.dto.request.SignUpRequest;
import org.operation.web.admin.dto.response.LoginResponse;
import org.operation.web.admin.dto.response.RefreshResponse;
import org.operation.web.admin.dto.response.SignUpResponse;
import org.operation.common.exception.AdminFailureException;
import org.operation.web.admin.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final JwtTokenProvider jwtTokenProvider; //TODO auth
    private final PasswordEncoder passwordEncoder; //TODO auth
    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public SignUpResponse signUp(SignUpRequest request){
        checkEmailDuplicated(request.email());
        val admin = adminRepository.save(request.toEntity());
        return SignUpResponse.of(admin);
    }

    private void checkEmailDuplicated(String email) {
        val isExist = adminRepository.existsByEmail(email);
        if (isExist) {
            throw new AdminFailureException("중복되는 이메일입니다"); //TODO: ExceptionMessage
        }
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        val admin = findByEmail(request.email());
        checkPasswordMatched(request.password(), admin);
        checkAdminAllowed(admin);
        val refreshToken = generateRefreshToken();
        admin.updateRefreshToken(refreshToken);
        val accessToken = generateAccessToken(admin);
        return LoginResponse.of(admin, accessToken);
    }

    private String generateAccessToken(Admin admin) {
        val adminAuthentication = new AdminAuthentication(admin.getId(), null, null);
        return jwtTokenProvider.generateAccessToken(adminAuthentication);
    }

    private String generateRefreshToken() {
        val authentication = new AdminAuthentication(admin.getId(), null, null);
        return jwtTokenProvider.generateRefreshToken(authentication);
    }

    private Admin findByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminFailureException("이메일이 존재하지 않습니다")); //TODO: message
    }

    private void checkPasswordMatched(String password, Admin admin) { // TODO: admin 내부로 옮기는 게 좋지 않을까..?
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new AdminFailureException("비밀번호가 일치하지 않습니다");
        }
    }

    private void checkAdminAllowed(Admin admin) {
        if (admin.isNotAllowed()) {
            throw new AdminFailureException("승인되지 않은 계정입니다");
        }
    }

    @Override
    @Transactional
    public RefreshResponse refresh(String refreshToken) {
        val adminId = jwtTokenProvider.getId(refreshToken, JwtTokenType.REFRESH_TOKEN);
        val admin = findById((Long)adminId);
        validateRefreshToken(admin, refreshToken);
        val newAccessToken = generateAccessToken();

        //TODO: 재발급 때 access만 해주지 않나??
        /*
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(adminAuthentication);
        admin.updateRefreshToken(newRefreshToken);*/

        return RefreshResponse.of(newAccessToken);
    }

    public void validateRefreshToken(Admin admin, String refreshToken) {
        if(!admin.getRefreshToken().equals(refreshToken)) {
            throw new AdminFailureException("토큰이 일치하지 않습니다"); //TODO: message
        }
    }

    private Admin findById(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminFailureException(INVALID_MEMBER.getName()));
    }
}
