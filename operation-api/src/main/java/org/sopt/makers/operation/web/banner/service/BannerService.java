package org.sopt.makers.operation.web.banner.service;

import java.util.List;

import org.sopt.makers.operation.web.banner.dto.request.*;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse.BannerImageUrl;

public interface BannerService {

    BannerResponse.BannerDetail getBannerDetail(final long bannerId);

    void deleteBanner(final long bannerId);

    List<BannerImageUrl> getExternalBanners(final String platform, final String location);

    BannerResponse.ImagePreSignedUrl getIssuedPreSignedUrlForPutImage(String contentName, String imageType, String imageExtension, String contentType);

    BannerResponse.BannerDetail createBanner(BannerRequest.BannerCreate request);
  
}
