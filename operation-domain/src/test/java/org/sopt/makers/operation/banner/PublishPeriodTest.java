package org.sopt.makers.operation.banner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.sopt.makers.operation.banner.domain.PublishPeriod;
import org.sopt.makers.operation.banner.domain.PublishStatus;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PublishPeriodTest {

    private static final LocalDate TEST_START_DATE = LocalDate.of(2024,1,1);
    private static final LocalDate TEST_END_DATE = LocalDate.of(2024,12,31);
    private final PublishPeriod givenPeriod = new PublishPeriod(TEST_START_DATE, TEST_END_DATE);

    @ParameterizedTest(name = "({index}) date : {0} -> result : {1}")
    @MethodSource("argsForCalculateStatus")
    @DisplayName("[TEST] 기간에 대한 상태 연산")
    void calculateCorrectStatus(
            // given
            LocalDate givenDate,
            PublishStatus expectedStatus
    ) {
        // when
        PublishStatus resultStatus = givenPeriod.getPublishStatus(givenDate);

        // then
        assertThat(resultStatus).isEqualTo(expectedStatus);
    }

    static Stream<Arguments> argsForCalculateStatus() {
        return Stream.of(
            Arguments.of(LocalDate.of(2023,12,31), PublishStatus.RESERVED),
                Arguments.of(LocalDate.of(2024,6,30), PublishStatus.IN_PROGRESS),
                Arguments.of(LocalDate.of(2025,1,1), PublishStatus.DONE)
        );
    }

}
