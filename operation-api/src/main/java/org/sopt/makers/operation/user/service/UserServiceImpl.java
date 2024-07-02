package org.sopt.makers.operation.user.service;

import java.util.List;

import lombok.val;
import lombok.RequiredArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import org.sopt.makers.operation.user.repository.UserRepository;
import org.sopt.makers.operation.user.repository.history.UserGenerationHistoryRepository;
import org.sopt.makers.operation.user.dto.response.UserInfoResponse;
import org.sopt.makers.operation.user.dto.response.UserInfosResponse;

import org.sopt.makers.operation.code.failure.UserFailureCode;
import org.sopt.makers.operation.exception.UserException;

@Service
@RequiredArgsConstructor
@Validated
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserGenerationHistoryRepository generationHistoryRepository;

    @Override
    public UserInfoResponse getUserInfo(@Min(1) Long userId) {
        val targetUser = userRepository.findUserById(userId);
        val histories = generationHistoryRepository.findAllHistoryByUserId(userId);
        return UserInfoResponse.of(targetUser, histories);
    }

    @Override
    public UserInfosResponse getUserInfos(@NotEmpty List<Long> userIds) {
        validateIds(userIds);
        val targetUsers = userRepository.findAllUsersById(userIds);
        val userInfoResponses = targetUsers.stream()
                .map(user -> {
                    val histories = generationHistoryRepository.findAllHistoryByUserId(user.getId());
                    return UserInfoResponse.of(user, histories);
                }).toList();
        return UserInfosResponse.of(userInfoResponses);
    }

    private void validateIds(List<Long> ids) {
        val isContainLittleThenOne  = ids.stream().anyMatch(id -> id < 1);
        if (isContainLittleThenOne) {
            throw new UserException(UserFailureCode.INVALID_USER_INCLUDED);
        }
    }

}
