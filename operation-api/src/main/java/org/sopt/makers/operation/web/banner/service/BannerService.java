package org.sopt.makers.operation.web.banner.service;

import org.sopt.makers.operation.web.banner.dto.request.BannerRequest;

import java.util.Arrays;
import java.util.List;

import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.exception.BannerException;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse.BannerImageUrl;

public interface BannerService {

    BannerResponse.BannerDetail getBannerDetail(final long bannerId);

    void deleteBanner(final long bannerId);

    List<BannerImageUrl> getExternalBanners(final String platform, final String location);

    BannerResponse.ImagePreSignedUrl getIssuedPreSignedUrlForPutImage(String contentName, String imageType, String imageExtension, String contentType);

    BannerResponse.BannerDetail createBanner(BannerRequest.BannerCreateOrModify request);

    BannerResponse.BannerDetail updateBanner(Long bannerId, BannerRequest.BannerCreateOrModify request);

    List<BannerResponse.BannerSimple> getBanners(final FilterCriteria status, final SortCriteria sort);

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
