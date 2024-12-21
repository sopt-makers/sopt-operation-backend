package org.sopt.makers.operation.web.banner.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.banner.domain.Banner;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public final class BannerResponse {

    @Builder(access = PRIVATE)
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

        public static BannerDetail fromEntity(Banner banner) {
            return BannerDetail.builder()
                    .bannerId(banner.getId())
                    .bannerStatus(banner.getPeriod().getPublishStatus(LocalDate.now()).getValue())
                    .bannerLocation(banner.getLocation().getValue())
                    .bannerType(banner.getContentType().getValue())
                    .publisher(banner.getPublisher())
                    .startDate(banner.getPeriod().getStartDate())
                    .endDate(banner.getPeriod().getEndDate())
                    .pcImageUrl(banner.getImage().getPcImageUrl())
                    .mobileImageUrl(banner.getImage().getMobileImageUrl())
                    .build();
        }


    }
    public record BannerImageUrl(
        @JsonProperty("url") String url
    ){

        public static List<BannerImageUrl> fromEntity(List<String> urlList){
            return urlList.stream()
                .map(BannerImageUrl::new)
                .collect(Collectors.toUnmodifiableList());
        }
    }
}
