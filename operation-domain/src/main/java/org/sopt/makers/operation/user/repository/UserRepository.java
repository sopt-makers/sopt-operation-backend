package org.sopt.makers.operation.user.repository;

import org.sopt.makers.operation.code.failure.UserFailureCode;
import org.sopt.makers.operation.exception.UserException;
import org.sopt.makers.operation.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {
    default User findUserById(Long userId) {
        return findById(userId).orElseThrow(() -> new UserException(UserFailureCode.INVALID_USER));
    }
}
