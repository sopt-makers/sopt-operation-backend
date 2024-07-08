package org.sopt.makers.operation.user.service;

import java.util.List;

import lombok.val;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.sopt.makers.operation.user.repository.UserRepository;
import org.sopt.makers.operation.user.repository.history.UserGenerationHistoryRepository;
import org.sopt.makers.operation.user.dto.response.UserInfoResponse;
import org.sopt.makers.operation.user.dto.response.UserInfosResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserGenerationHistoryRepository generationHistoryRepository;

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        val targetUser = userRepository.findUserById(userId);
        val histories = generationHistoryRepository.findAllHistoryByUserId(userId);
        return UserInfoResponse.of(targetUser, histories);
    }

    @Override
    public UserInfosResponse getUserInfos(List<Long> userIds) {
        return null;
    }
}
