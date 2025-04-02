package org.sopt.makers.operation.banner.domain;

import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.code.failure.FailureCode;
import org.sopt.makers.operation.exception.BannerException;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;
import static org.sopt.makers.operation.code.failure.BannerFailureCode.NOT_SUPPORTED_PLATFORM_TYPE;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class BannerImage {
    private String pcImageUrl;
    private String mobileImageUrl;

    public void updatePcImage(String updatePcImageUrl) {
        this.pcImageUrl = updatePcImageUrl;
    }

    public void updateMobileImage(String updateMobileImageUrl) {
        this.mobileImageUrl = updateMobileImageUrl;
    }

    public String retrieveImageUrl(String platform) {
      return switch (platform) {
        case "pc" -> pcImageUrl;
        case "mobile" -> mobileImageUrl;
        default -> throw new BannerException(NOT_SUPPORTED_PLATFORM_TYPE);
      };
    }
  
    @Builder
    private BannerImage(String pcImageUrl, String mobileImageUrl) {
        this.pcImageUrl = pcImageUrl;
        this.mobileImageUrl = mobileImageUrl;
    }
}
