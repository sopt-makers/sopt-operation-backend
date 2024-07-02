package org.sopt.makers.operation.user.repository.history;

import java.util.List;

import lombok.val;
import org.sopt.makers.operation.exception.UserException;
import org.sopt.makers.operation.code.failure.UserFailureCode;
import org.sopt.makers.operation.user.domain.UserGenerationHistory;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserGenerationHistoryRepository extends JpaRepository<UserGenerationHistory, Long> {

    List<UserGenerationHistory> findAllByUserId(Long userId);

    default List<UserGenerationHistory> findAllHistoryByUserId(Long userId) {
        val histories = findAllByUserId(userId);
        if (histories.isEmpty()) {
            throw new UserException(UserFailureCode.NOT_FOUND_HISTORY);
        }
        return histories;
    }

}
