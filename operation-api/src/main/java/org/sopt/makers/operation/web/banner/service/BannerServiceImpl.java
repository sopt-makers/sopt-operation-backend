package org.sopt.makers.operation.web.banner.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.banner.domain.ContentType;
import org.sopt.makers.operation.banner.domain.ImageExtension;
import org.sopt.makers.operation.banner.domain.ImageType;
import org.sopt.makers.operation.banner.domain.Banner;
import org.sopt.makers.operation.banner.repository.BannerRepository;
import org.sopt.makers.operation.client.s3.S3Service;
import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.BannerException;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final S3Service s3Service;
    private final ValueConfig valueConfig;

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
        val type = ImageType.getByValue(imageType);
        val extension = ImageExtension.getByValue(imageExtension);
        val location = ContentType.getByValue(contentType).getLocation();
        val fileName = getBannerImageName(location, contentName, type.getValue(), extension.getValue());
        val putPreSignedUrl = s3Service.createPutPreSignedUrl(valueConfig.getBannerBucket(), fileName);

        return BannerResponse.ImagePreSignedUrl.of(putPreSignedUrl, fileName);
    }

    private String getBannerImageName(String location, String contentName, String imageType, String imageExtension) {
        val today = LocalDate.now();
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        val formattedDate = today.format(formatter);

        return location+formattedDate + "_" + contentName + "(" + imageType + ")." + imageExtension;
    }
}
