package org.sopt.makers.operation.banner.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class BannerImage {
    private String pcImageUrl;
    private String mobileImageUrl;

    public void updatePcImage(String updatePcImageUrl) {
        this.pcImageUrl = updatePcImageUrl;
    }

    public void updateMobileImage(String updateMobileImageUrl) {
        this.mobileImageUrl = updateMobileImageUrl;
    }

}
