package org.sopt.makers.operation.user.service;

import java.util.List;
import java.util.Collections;

import lombok.val;
import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.user.dao.UserActivityInfoUpdateDao;
import org.sopt.makers.operation.user.dao.UserPersonalInfoUpdateDao;
import org.sopt.makers.operation.user.domain.Position;
import org.sopt.makers.operation.user.domain.User;
import org.sopt.makers.operation.user.domain.UserGenerationHistory;
import org.sopt.makers.operation.user.dto.request.UserActivityModifyRequest;
import org.sopt.makers.operation.user.dto.request.UserModifyRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.sopt.makers.operation.user.repository.UserRepository;
import org.sopt.makers.operation.user.repository.history.UserGenerationHistoryRepository;
import org.sopt.makers.operation.user.dto.response.UserInfoResponse;
import org.sopt.makers.operation.user.dto.response.UserInfosResponse;

import org.sopt.makers.operation.code.failure.UserFailureCode;
import org.sopt.makers.operation.exception.UserException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserGenerationHistoryRepository generationHistoryRepository;

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        validateIds(Collections.singletonList(userId));
        val targetUser = userRepository.findUserById(userId);
        val histories = generationHistoryRepository.findAllHistoryByUserId(userId);
        return UserInfoResponse.of(targetUser, histories);
    }

    @Override
    public UserInfosResponse getUserInfos(List<Long> userIds) {
        validateIds(userIds);
        val targetUsers = userRepository.findAllUsersById(userIds);
        val userInfoResponses = targetUsers.stream()
                .map(this::mergeUserInfoWithHistory).toList();
        return UserInfosResponse.of(userInfoResponses);
    }

    @Override
    @Transactional
    public void modifyUserPersonalInfo(UserModifyRequest userModifyRequest) {
        val targetUser = userRepository.findUserById(userModifyRequest.userId());
        val userInfoUpdateDao = new UserPersonalInfoUpdateDao(
                userModifyRequest.userName(),
                userModifyRequest.userPhone(),
                userModifyRequest.userProfileImage()
        );
        targetUser.updateUserInfo(userInfoUpdateDao);
    }

    @Override
    @Transactional
    public void modifyUserActivityInfos(List<UserActivityModifyRequest> activitiesModifyRequest) {
        activitiesModifyRequest.forEach(
                activityModifyRequest -> {
                    val targetActivity = generationHistoryRepository.findHistoryById(activityModifyRequest.activityId());
                    validateIsAbleToUpdateActivity(targetActivity, activityModifyRequest);

                    val activityInfoUpdateDao = new UserActivityInfoUpdateDao(
                            activityModifyRequest.team()
                    );
                    targetActivity.updateActivityInfo(activityInfoUpdateDao);
                });
    }

    private void validateIsAbleToUpdateActivity(
            UserGenerationHistory currentActivity, UserActivityModifyRequest activityModifyRequest
    ) {
        if (!Position.MEMBER.equals(currentActivity.getPosition())
                && !currentActivity.getTeam().equals(activityModifyRequest.team())
        ) {
            throw new UserException(UserFailureCode.INVALID_USER_MODIFY_ACTIVITY_INFO);
        }
    }

    private void validateIds(List<Long> ids) {
        if (ids.isEmpty() || ids.contains(null)) {
            throw new UserException(UserFailureCode.INVALID_PARAMETER);
        }
        val isContainLittleThenOne  = ids.stream().anyMatch(id -> id < 1);
        if (isContainLittleThenOne) {
            throw new UserException(UserFailureCode.INVALID_USER_IN_USER_LIST_PARAMETER);
        }
    }

    private UserInfoResponse mergeUserInfoWithHistory(User user) {
        val histories = generationHistoryRepository.findAllHistoryByUserId(user.getId());
        return UserInfoResponse.of(user, histories);
    }

}
