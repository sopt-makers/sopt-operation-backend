package org.sopt.makers.operation.web.banner.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public final class BannerResponse {

    public record BannerDetail(
            @JsonProperty("id") long bannerId,
            @JsonProperty("status") String bannerStatus,
            @JsonProperty("location") String bannerLocation,
            @JsonProperty("content_type") String bannerType,
            @JsonProperty("publisher") String publisher,
            @JsonProperty("start_date") LocalDate startDate,
            @JsonProperty("end_date") LocalDate endDate,
            @JsonProperty("image_url_pc") String pcImageUrl,
            @JsonProperty("image_url_mobile") String mobileImageUrl
    ) {

    }
}
