package org.sopt.makers.operation.web.banner.dto.request;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.*;
import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.exception.BannerException;
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

            @Schema(description = "시작일 (yyyy-MM-dd)", type = "string", format = "date", required = true)
            @NotBlank @JsonProperty("start_date") String start_date,

            @Schema(description = "종료일 (yyyy-MM-dd)", type = "string", format = "date", required = true)
            @NotBlank @JsonProperty("end_date") String end_date,

            @Schema(description = "링크", required = false)
            @JsonProperty("link") String link,

            @Schema(description = "PC용 이미지", type = "string", format = "binary", required = true)
            @JsonProperty("image_pc") MultipartFile image_pc,

            @Schema(description = "모바일용 이미지", type = "string", format = "binary", required = true)
            @JsonProperty("image_mobile") MultipartFile image_mobile
    ) {
        /**
         * 시작일을 LocalDate로 파싱한다. 포맷이 yyyy-MM-dd가 아니면 BannerException.
         */
        public LocalDate getStartDate() {
            return parseDate(start_date);
        }

        /**
         * 종료일을 LocalDate로 파싱한다. 포맷이 yyyy-MM-dd가 아니면 BannerException.
         */
        public LocalDate getEndDate() {
            return parseDate(end_date);
        }

        private static LocalDate parseDate(String value) {
            try {
                return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException | NullPointerException e) {
                throw new BannerException(BannerFailureCode.INVALID_BANNER_DATE_FORMAT);
            }
        }
    }
}
