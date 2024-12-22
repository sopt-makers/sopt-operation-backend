package org.sopt.makers.operation.web.banner.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.banner.domain.*;
import org.sopt.makers.operation.banner.repository.BannerRepository;
import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.exception.BannerException;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    @Override
    public BannerResponse.BannerDetail getBannerDetail(final long bannerId) {
        val banner = getBannerById(bannerId);
        return BannerResponse.BannerDetail.fromEntity(banner);
    }

    private Banner getBannerById(final long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new BannerException(BannerFailureCode.NOT_FOUNT_BANNER));
    }

    @Override
    public BannerResponse.ImagePreSignedUrl getPutPreSignedUrlForBanner(String contentName, String imageType, String imageExtension, String contentType) {
        return null;
    }

    private String getBannerImageName(String contentName, String imageType, String imageExtension, String contentType) {
        return null;
    }
}
