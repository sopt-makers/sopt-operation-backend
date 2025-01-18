package org.sopt.makers.operation.web.banner.dto.request;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.*;
import java.time.*;
import lombok.*;

@RequiredArgsConstructor(access = PRIVATE)
public class BannerRequest {

    public record BannerCreateOrModify(
            @JsonProperty("location") String bannerLocation,
            @JsonProperty("content_type") String bannerType,
            @JsonProperty("publisher") String publisher,
            @JsonProperty("start_date") LocalDate startDate,
            @JsonProperty("end_date") LocalDate endDate,
            @JsonProperty("link") String link,
            @JsonProperty("image_pc") String pcImage,
            @JsonProperty("image_mobile") String mobileImage
    ) {}
}
