package org.sopt.makers.operation.user.repository;

import lombok.val;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;

import org.sopt.makers.operation.user.domain.User;
import org.sopt.makers.operation.user.domain.Gender;
import org.sopt.makers.operation.user.repository.history.UserGenerationHistoryRepository;
import org.sopt.makers.operation.exception.UserException;
import org.sopt.makers.operation.code.failure.UserFailureCode;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.*;

@DataJpaTest
@DisplayName("[[ Unit Test ]] - UserRepository >")
@EntityScan(basePackages = "org.sopt.makers.operation.user")
@ContextConfiguration(classes = {
        UserRepository.class, UserGenerationHistoryRepository.class
})
@EnableAutoConfiguration
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Nested
    @Order(1)
    @DisplayName("[TEST] 단일 유저 대상 단일 ID로 조회하는 시나리오")
    @TestInstance(Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SingleUserTest {

        private User user;
        @BeforeEach
        void setUpSingleUser() {
            User userEntity = User.builder()
                    .email("test@test.com")
                    .phone("010-0000-0000")
                    .gender(Gender.MALE)
                    .name("TestUser")
                    .profileImage("DummyImageUrl")
                    .birthday(LocalDate.of(1999, 12,4))
                    .build();
            user = userRepository.save(userEntity);
        }

        @Test
        @Order(1)
        @DisplayName("Case1. 단일 유저에 대한 저장 및 조회 성공")
        void getSuccessTest() {
            // given
            val userId = user.getId();

            // when
            val result = userRepository.findUserById(userId);

            // then
            assertThat(result.getId()).isEqualTo(userId);
            assertThat(result.getName()).isEqualTo("TestUser");
            assertThat(result.getPhone()).isEqualTo("010-0000-0000");
            assertThat(result.getEmail()).isEqualTo("test@test.com");
            assertThat(result.getBirthday()).isEqualTo(LocalDate.of(1999, 12, 4));
            assertThat(result.getGender()).isEqualTo(Gender.MALE);
            assertThat(result.getProfileImage()).isEqualTo("DummyImageUrl");
        }

        @Test
        @Order(2)
        @DisplayName("Case2. 존재하지 않은 userId일 경우, 예외 반환")
        void getFailTest() {
            // given
            val invalidUserId = 0L;

            // when & then
            val userException = assertThrows(UserException.class, () -> userRepository.findUserById(invalidUserId));
            assertThat(userException.getFailureCode()).isEqualTo(UserFailureCode.INVALID_USER);
        }

    }

    @Nested
    @Order(2)
    @TestInstance(Lifecycle.PER_CLASS)
    @DisplayName("[TEST] 여러 유저가 저장된 상태에서 단일 ID로 조회하는 시나리오")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class MultiUserTest {

        private Map<Long, User> cachedUsers;

        @BeforeAll
        void setUpMultiUser() {
            cachedUsers = new HashMap<>();
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                val dummyUser = User.builder()
                        .email(String.format("test%d@test.com", i))
                        .phone(String.format("010-000%d-000%d", i, i))
                        .gender(i % 2 == 0 ? Gender.MALE : Gender.FEMALE)
                        .name(String.format("TestUser%d", i))
                        .profileImage(String.format("DummyImageUrl%d", i))
                        .birthday(LocalDate.of(1999, 12, i + 1))
                        .build();
                users.add(dummyUser);
            }
            List<User> savedUsers = userRepository.saveAll(users);
            savedUsers.forEach(user -> cachedUsers.put(user.getId(), user));
        }

        @Test
        @Order(1)
        @DisplayName("Case1. 여러 유저에 대한 저장 및 각 유저에 대한 조회 성공")
        void getSuccessTest() {
            for (Long userId : cachedUsers.keySet()) {
                // given
                val user = cachedUsers.get(userId);

                // when
                val result = userRepository.findUserById(userId);

                // then
                assertThat(result.getId()).isEqualTo(user.getId());
                assertThat(result.getName()).isEqualTo(user.getName());
                assertThat(result.getPhone()).isEqualTo(user.getPhone());
                assertThat(result.getEmail()).isEqualTo(user.getEmail());
                assertThat(result.getBirthday()).isEqualTo(user.getBirthday());
                assertThat(result.getGender()).isEqualTo(user.getGender());
                assertThat(result.getProfileImage()).isEqualTo(user.getProfileImage());
            }

        }

        @Test
        @DisplayName("Case2. 여러 유저 중 존재하지 않은 userId로 조회할 경우, 예외 반환")
        void getFailTest() {
            // given
            Long invalidId = 0L;
            Set<Long> ids = new HashSet<>();
            ids.add(invalidId);
            ids.addAll(cachedUsers.keySet());

            for (Long userId : ids) {
                if (userId.equals(invalidId)) {
                    // when & then
                    val userException = assertThrows(UserException.class, () -> userRepository.findUserById(invalidId));
                    assertThat(userException.getFailureCode()).isEqualTo(UserFailureCode.INVALID_USER);
                    return;
                }
            }
        }
    }


}