package org.sopt.makers.operation.web.banner.service;

import org.sopt.makers.operation.web.banner.dto.request.BannerRequest.*;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;

public interface BannerService {

    BannerResponse.BannerDetail getBannerDetail(final long bannerId);

    BannerResponse.ImagePreSignedUrl getIssuedPreSignedUrlForPutImage(String contentName, String imageType, String imageExtension, String contentType);

    BannerResponse.BannerDetail createBanner(BannerCreateOrModify request);

    BannerResponse.BannerDetail updateBanner(Long bannerId, BannerCreateOrModify request);
}
