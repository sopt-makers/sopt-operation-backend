package org.sopt.makers.operation.web.banner.service;

import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;

public interface BannerService {

    BannerResponse.BannerDetail getBannerDetail(final long bannerId);

    void deleteBanner(final long bannerId);
}
