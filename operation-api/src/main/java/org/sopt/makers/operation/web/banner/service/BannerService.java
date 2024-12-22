package org.sopt.makers.operation.web.banner.service;

import org.sopt.makers.operation.banner.domain.*;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;

public interface BannerService {

    BannerResponse.BannerDetail getBannerDetail(final long bannerId);

    BannerResponse.ImagePreSignedUrl getPutPreSignedUrlForBanner(String contentName, String imageType, String imageExtension, String contentType);
}
