package org.sopt.makers.operation.common.util;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.code.failure.PermissionFailureCode;
import org.sopt.makers.operation.code.failure.UserFailureCode;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.PermissionException;
import org.sopt.makers.operation.exception.UserException;
import org.sopt.makers.operation.user.repository.history.UserGenerationHistoryRepository;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PermissionValidator {

    private final ValueConfig valueConfig;
    private final UserGenerationHistoryRepository generationHistoryRepository;

    public void validateIsSelf(long requestUserId, long targetUserId) {
        if (!checkIsSelf(requestUserId, targetUserId)) {
            throw new PermissionException(PermissionFailureCode.NO_PERMISSION_ONLY_SELF);
        }
    }

    public void validateIsSelfOrExecutive(long requestUserId, long targetUserId) {
        if (!checkIsSelf(requestUserId, targetUserId) || !checkIsExecutive(requestUserId)) {
            throw new PermissionException(PermissionFailureCode.NO_PERMISSION_ONLY_SELF_INCLUDE_EXECUTIVE);
        }
    }

    private boolean checkIsSelf(long requesterId, long targetId) {
        return requesterId == targetId;
    }

    private boolean checkIsExecutive(long requesterId) {
        val requesterCurrentHistory = generationHistoryRepository.findAllByUserId(requesterId).stream()
                .filter(history -> history.getGeneration() == valueConfig.getGENERATION())
                .findFirst()
                .orElseThrow(() -> new UserException(UserFailureCode.NOT_FOUND_HISTORY));
        return requesterCurrentHistory.isExecutive();
    }

}
