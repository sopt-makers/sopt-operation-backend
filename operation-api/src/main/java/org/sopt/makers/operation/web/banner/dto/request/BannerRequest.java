package org.sopt.makers.operation.web.banner.dto.request;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.*;
import java.time.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor(access = PRIVATE)
public class BannerRequest {

    public record BannerCreateOrModify(
            @JsonProperty("location") String location,
            @JsonProperty("content_type") String content_type,
            @JsonProperty("publisher") String publisher,
            @JsonProperty("start_date") LocalDate start_date,
            @JsonProperty("end_date") LocalDate end_date,
            @JsonProperty("link") String link,
            @JsonProperty("image_pc") MultipartFile image_pc,
            @JsonProperty("image_mobile") MultipartFile image_mobile
    ) {}
}
