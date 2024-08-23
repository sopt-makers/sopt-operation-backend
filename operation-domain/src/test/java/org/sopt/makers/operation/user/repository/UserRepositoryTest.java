package org.sopt.makers.operation.user.repository;

import lombok.val;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.sopt.makers.operation.user.domain.User;
import org.sopt.makers.operation.user.domain.Gender;
import org.sopt.makers.operation.user.repository.history.UserGenerationHistoryRepository;
import org.sopt.makers.operation.user.dao.UserPersonalInfoUpdateDao;
import org.sopt.makers.operation.DatabaseCleaner;
import org.sopt.makers.operation.exception.UserException;
import org.sopt.makers.operation.code.failure.UserFailureCode;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

@DataJpaTest
@DisplayName("[[ Unit Test ]] - UserRepository")
@EntityScan(basePackages = "org.sopt.makers.operation.user")
@ContextConfiguration(classes = {
        UserRepository.class, UserGenerationHistoryRepository.class,
        DatabaseCleaner.class
})
@EnableAutoConfiguration
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseCleaner cleaner;

    @Nested
    @DisplayName("[TEST] 단일 유저 대상 단일 ID로 조회하는 시나리오")
    @TestInstance(Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SingleUserSelectTest {

        @BeforeAll
        void setUpCleanerProperties() {
            cleaner.execute();
        }

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
        @DisplayName("Case1. 단일 유저에 대한 조회 성공")
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
        @DisplayName("Case2. 존재하지 않은 userId일 경우, 예외 반환")
        void getFailTest() {
            // given
            val invalidUserId = 0L;

            // when & then
            assertThatThrownBy(() -> userRepository.findUserById(invalidUserId))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserFailureCode.NOT_FOUND_USER.getMessage());
        }

    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    @DisplayName("[TEST] 여러 유저가 저장된 상태에서 단일 ID로 조회하는 시나리오")
    class MultiUserSelectTest {

        List<User> users;

        @BeforeAll
        void setUpMultiUser() {
            cleaner.execute();
            User userA = User.builder()
                    .email("test1@test.com")
                    .phone("010-0000-0000")
                    .gender(Gender.MALE)
                    .name("TestUser1")
                    .profileImage("DummyImageUrl1")
                    .birthday(LocalDate.of(1999, 12,4))
                    .build();
            User userB = User.builder()
                    .email("test2@test.com")
                    .phone("010-1234-0000")
                    .gender(Gender.FEMALE)
                    .name("TestUser2")
                    .profileImage("DummyImageUrl2")
                    .birthday(LocalDate.of(1999, 12,4))
                    .build();
            User userC = User.builder()
                    .email("test3@test.com")
                    .phone("010-5678-0000")
                    .gender(Gender.MALE)
                    .name("TestUser3")
                    .profileImage("DummyImageUrl3")
                    .birthday(LocalDate.of(1999, 12,4))
                    .build();
            users = userRepository.saveAll(List.of(userA, userB, userC));
        }

        @Test
        @DisplayName("Case1. 복수 유저 조회 성공")
        void getSuccessTest() {
            // given
            val ids = List.of(1L, 2L, 3L);
            List<User> allUser = userRepository.findAll();
            allUser.forEach(u -> System.out.println(u.getId()));
            // when
            val result = userRepository.findAllUsersById(ids);


            // then
            assertThat(users).usingRecursiveComparison()
                    .isEqualTo(result);
        }

        @Test
        @DisplayName("Case2. 존재하지 않는 userId를 가진 경우, 예외 반환")
        void getFailTest_Include_NotExist_Id() {
            // given
            val ids = List.of(1L, 2L, 3L, 999L);

            // when & then
            assertThatThrownBy(() -> userRepository.findAllUsersById(ids))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserFailureCode.NOT_FOUND_USER_IN_USER_LIST_PARAMETER.getMessage());
        }

    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    @DisplayName("[TEST] 단일 유저 정보 수정하는 시나리오")
    class SingleUserUpdateTest {

        private static final long ABSOLUTE_USER_ID_VALUE = 1L;

        private User user;

        @BeforeEach
        void setUpUser() {
            cleaner.afterPropertiesSet();
            cleaner.execute();
            User userEntity = User.builder()
                    .email("test@test.com")
                    .gender(Gender.MALE)
                    .phone("010-0000-0000")
                    .name("TestUser")
                    .profileImage("DummyImageUrl")
                    .birthday(LocalDate.of(1999, 12,4))
                    .build();
            user = userRepository.save(userEntity);
        }

        @ParameterizedTest
        @DisplayName("Case. 단일 유저에 대한 전체 데이터 변경 성공")
        @MethodSource("ofAllDataUpdate")
        @Rollback(value = false)
        void updateAllDataSuccess(
                UserPersonalInfoUpdateDao infoUpdateDao,
                String expectedName,
                String expectedPhone,
                String expectedProfileImageUrl
        ) {
            // given
            User targetUser = userRepository.findUserById(ABSOLUTE_USER_ID_VALUE);

            // when
            targetUser.updateUserInfo(infoUpdateDao);
            userRepository.save(user);
            User expectedUser = userRepository.findUserById(ABSOLUTE_USER_ID_VALUE);

            // then
            assertThat(expectedUser.getName()).isEqualTo(expectedName);
            assertThat(expectedUser.getPhone()).isEqualTo(expectedPhone);
            assertThat(expectedUser.getProfileImage()).isEqualTo(expectedProfileImageUrl);
        }
        static Stream<Arguments> ofAllDataUpdate(){
            return Stream.of(
                    Arguments.of(
                            new UserPersonalInfoUpdateDao("김철수", "01098765432", "changedProfileImageForCheolSu"),
                            "김철수", "01098765432", "changedProfileImageForCheolSu"
                    )
            );
        }

        @ParameterizedTest
        @DisplayName("Case. 단일 유저에 대한 부분 데이터 변경 성공")
        @MethodSource("ofPartialDataUpdate")
        @Rollback(value = false)
        void updatePartialDataSuccess(
                UserPersonalInfoUpdateDao infoUpdateDao,
                String expectedName,
                String expectedPhone,
                String expectedProfileImageUrl
        ) {
            // given
            User targetUser = userRepository.findUserById(ABSOLUTE_USER_ID_VALUE);

            // when
            targetUser.updateUserInfo(infoUpdateDao);
            userRepository.save(targetUser);
            User expectedUser = userRepository.findUserById(ABSOLUTE_USER_ID_VALUE);

            // then
            assertThat(expectedUser.getName()).isEqualTo(expectedName);
            assertThat(expectedUser.getPhone()).isEqualTo(expectedPhone);
            assertThat(expectedUser.getProfileImage()).isEqualTo(expectedProfileImageUrl);
        }
        static Stream<Arguments> ofPartialDataUpdate(){
            return Stream.of(
                    Arguments.of(
                            new UserPersonalInfoUpdateDao("김철수", "010-0000-0000", "DummyImageUrl"),
                            "김철수", "010-0000-0000", "DummyImageUrl"
                    ),
                    Arguments.of(
                            new UserPersonalInfoUpdateDao("TestUser", "010-1324-5768", "DummyImageUrl"),
                            "TestUser", "010-1324-5768", "DummyImageUrl"
                    ),
                    Arguments.of(
                            new UserPersonalInfoUpdateDao("TestUser", "010-0000-0000", "changedProfileImage"),
                            "TestUser", "010-0000-0000", "changedProfileImage"
                    )
            );
        }

    }

}