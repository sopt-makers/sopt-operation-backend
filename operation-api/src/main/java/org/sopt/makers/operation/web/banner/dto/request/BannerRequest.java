package org.sopt.makers.operation.web.banner.dto.request;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor(access = PRIVATE)
public class BannerRequest {

    public record BannerCreateOrModify(
            @Schema(description = "배너 위치", required = true)
            @JsonProperty("location") String location,

            @Schema(description = "컨텐츠 타입", required = true)
            @JsonProperty("content_type") String content_type,

            @Schema(description = "게시자", required = true)
            @JsonProperty("publisher") String publisher,

            @Schema(description = "시작일", type = "string", format = "date", required = true)
            @JsonProperty("start_date") LocalDate start_date,

            @Schema(description = "종료일", type = "string", format = "date", required = true)
            @JsonProperty("end_date") LocalDate end_date,

            @Schema(description = "링크", required = false)
            @JsonProperty("link") String link,

            @Schema(description = "PC용 이미지", type = "string", format = "binary", required = true)
            @JsonProperty("image_pc") MultipartFile image_pc,

            @Schema(description = "모바일용 이미지", type = "string", format = "binary", required = true)
            @JsonProperty("image_mobile") MultipartFile image_mobile
    ) {}
}
