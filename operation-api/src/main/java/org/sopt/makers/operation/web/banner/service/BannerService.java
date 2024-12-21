package org.sopt.makers.operation.web.banner.service;

import org.sopt.makers.operation.web.banner.dto.request.*;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;

public interface BannerService {

    BannerResponse.BannerDetail getBannerDetail(final long bannerId);

    BannerResponse.ImagePreSignedUrl getPreSignedUrlForBanner(String bannerName, ImageType imageType, ImageExtension imageExtension);
}
