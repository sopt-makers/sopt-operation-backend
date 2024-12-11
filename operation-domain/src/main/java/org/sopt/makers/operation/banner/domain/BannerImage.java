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

    public void changePcImageTo(String changedPcImageUrl) {
        this.pcImageUrl = changedPcImageUrl;
    }

    public void changeMobileImageTo(String changedMobileImageUrl) {
        this.pcImageUrl = changedMobileImageUrl;
    }

}
