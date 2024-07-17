package org.sopt.makers.operation.user.repository;

import lombok.val;
import org.sopt.makers.operation.code.failure.UserFailureCode;
import org.sopt.makers.operation.exception.UserException;
import org.sopt.makers.operation.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {

    default User findUserById(Long userId) {
        return findById(userId).orElseThrow(() -> new UserException(UserFailureCode.NOT_FOUND_USER));
    }
    default List<User> findAllUsersById(List<Long> userIds) {
        val allUsers = findAllById(userIds);
        if (allUsers.size() != userIds.size()) {
            throw new UserException(UserFailureCode.NOT_FOUND_USER_IN_USER_LIST_PARAMETER);
        }
        return allUsers;
    }

}
