package org.sopt.makers.operation.web.banner.service;

import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.web.banner.dto.request.BannerRequest;

import java.util.Arrays;
import java.util.List;

import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.exception.BannerException;
import org.sopt.makers.operation.web.banner.dto.response.BannerPaginationResponse;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse.BannerImageUrl;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse.BannerSimple;
import org.springframework.http.ResponseEntity;

public interface BannerService {

    BannerResponse.BannerDetail getBannerDetail(final long bannerId);

    ResponseEntity<BaseResponse<?>> deleteBanner(final long bannerId);


    List<BannerResponse.BannerImageWithBothPlatforms> getExternalBanners(final String location);

    BannerResponse.BannerDetail createBanner(BannerRequest.BannerCreateOrModify request);

    BannerResponse.BannerDetail updateBanner(Long bannerId, BannerRequest.BannerCreateOrModify request);

    BannerPaginationResponse<BannerSimple> getBannersWithPagination(
            FilterCriteria filter,
            SortCriteria sort,
            int page,
            int limit);
    enum FilterCriteria {
        ALL("all"),
        RESERVED("reserved"),
        IN_PROGRESS("in_progress"),
        DONE("done"),
        ;
        private final String parameter;

        FilterCriteria(String parameter) {
            this.parameter = parameter;
        }

        public String getParameter() {
            return this.parameter;
        }

        public static FilterCriteria fromParameter(String parameter) {
            return Arrays.stream(FilterCriteria.values())
                    .filter(value -> value.parameter.equals(parameter))
                    .findAny().orElseThrow(() -> new BannerException(BannerFailureCode.INVALID_BANNER_PROGRESS_STATUS_PARAMETER));
        }

    }
    enum SortCriteria {
        START_DATE("start_date"),
        END_DATE("end_date"),
        STATUS("status")
        ;
        private final String parameter;

        SortCriteria(String parameter) {
            this.parameter = parameter;
        }
        public static SortCriteria fromParameter(String parameter) {
            return Arrays.stream(SortCriteria.values())
                    .filter(value -> value.parameter.equals(parameter))
                    .findAny().orElseThrow(() -> new BannerException(BannerFailureCode.INVALID_BANNER_SORT_CRITERIA_PARAMETER));
        }
    }
}
