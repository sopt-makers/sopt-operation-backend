package org.sopt.makers.operation.web.banner.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sopt.makers.operation.banner.domain.PublishPeriod;
import org.sopt.makers.operation.banner.domain.PublishStatus;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sopt.makers.operation.web.banner.service.BannerService.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data/insert-get-banners-data.sql")
class BannerServiceTest {

    @Autowired
    private BannerService bannerService;


    @ParameterizedTest
    @MethodSource("argsForGetBanners")
    void getBanners(
            FilterCriteria givenFilter,
            SortCriteria givenSort,
            int expectedSize,
            PublishStatus expectedStatus
    ) {
        List<BannerResponse.BannerSimple> banners = bannerService.getBanners(givenFilter, givenSort);

        assertThat(banners)
                .hasSize(expectedSize)
                .extracting(info -> PublishPeriod.builder().startDate(info.startDate()).endDate(info.endDate()).build())
                .allMatch(period -> {
                    if (givenFilter.equals(FilterCriteria.ALL)) {
                        return true;
                    }
                    return period.getPublishStatus(LocalDate.now()).equals(expectedStatus);
                });
    }
    static Stream<Arguments> argsForGetBanners(){
        return Stream.of(
                Arguments.of(FilterCriteria.RESERVED, SortCriteria.START_DATE, 6, PublishStatus.RESERVED),
                Arguments.of(FilterCriteria.ALL, SortCriteria.START_DATE, 18, null),
                Arguments.of(FilterCriteria.IN_PROGRESS, SortCriteria.START_DATE, 6, PublishStatus.IN_PROGRESS),
                Arguments.of(FilterCriteria.DONE, SortCriteria.START_DATE, 6, PublishStatus.DONE)
        );
    }

}