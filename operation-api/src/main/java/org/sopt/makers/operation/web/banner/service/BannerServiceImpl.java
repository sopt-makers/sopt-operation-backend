package org.sopt.makers.operation.web.banner.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.banner.domain.*;
import org.sopt.makers.operation.banner.repository.BannerRepository;
import org.sopt.makers.operation.client.s3.S3Service;
import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.exception.BannerException;
import org.sopt.makers.operation.web.banner.dto.request.*;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerServiceImpl implements BannerService {
    private static final String SLASH = "/";
    private static final String PROTOCOL_SEPARATOR = "//";
    private static final int PROTOCOL_END_OFFSET = 2;
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
                .orElseThrow(() -> new BannerException(BannerFailureCode.NOT_FOUND_BANNER));
    }

    @Override
    public BannerResponse.ImagePreSignedUrl getIssuedPreSignedUrlForPutImage(String contentName, String imageType, String imageExtension, String contentType) {
        val type = ImageType.getByValue(imageType);
        val extension = ImageExtension.getByValue(imageExtension);
        val location = ContentType.getByValue(contentType).getLocation();
        val fileName = getBannerImageName(location, contentName, type.getValue(), extension.getValue());
        val putPreSignedUrl = s3Service.createPreSignedUrlForPutObject(valueConfig.getBannerBucket(), fileName);

        return BannerResponse.ImagePreSignedUrl.of(putPreSignedUrl, fileName);
    }

    private String getBannerImageName(String location, String contentName, String imageType, String imageExtension) {
        val today = LocalDate.now();
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        val formattedDate = today.format(formatter);

        return location+formattedDate + "_" + contentName + "(" + imageType + ")." + imageExtension;
    }

    @Transactional
    @Override
    public BannerDetail createBanner(BannerRequest.BannerCreateOrModify request) {
        val period = getPublishPeriod(request.startDate(), request.endDate());
        val image = getBannerImage(request.pcImage(), request.mobileImage());
        val newBanner = Banner.builder()
                .publisher(request.publisher())
                .link(request.link())
                .contentType(ContentType.getByValue(request.bannerType()))
                .location(PublishLocation.getByValue(request.bannerLocation()))
                .period(period)
                .image(image)
                .build();
        val banner = saveBanner(newBanner);

        return BannerResponse.BannerDetail.fromEntity(banner);
    }

    @Transactional
    @Override
    public BannerDetail updateBanner(Long bannerId, BannerRequest.BannerCreateOrModify request) {
        var banner = getBannerById(bannerId);
        val period = getPublishPeriod(request.startDate(), request.endDate());
        val image = getBannerImage(request.pcImage(), request.mobileImage());

        deleteExistImage(banner.getImage().getPcImageUrl());
        deleteExistImage(banner.getImage().getMobileImageUrl());
        banner.updatePublisher(request.publisher());
        banner.updateLink(request.link());
        banner.updateContentType(ContentType.getByValue(request.bannerType()));
        banner.updateLocation(PublishLocation.getByValue(request.bannerLocation()));
        banner.updatePeriod(period);
        banner.updateImage(image);
        return BannerResponse.BannerDetail.fromEntity(banner);
    }

    private void deleteExistImage(String url) {
        val protocolEndIndex = url.indexOf(PROTOCOL_SEPARATOR) + PROTOCOL_END_OFFSET;
        val firstSlashIndex = url.indexOf(SLASH, protocolEndIndex);
        val extractedPath = url.substring(firstSlashIndex);
        s3Service.deleteFile(valueConfig.getBannerBucket(), extractedPath);
    }

    private PublishPeriod getPublishPeriod(LocalDate startDate, LocalDate endDate) {
        return PublishPeriod.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    private BannerImage getBannerImage(String pcImage, String mobileImage) {
        val pcImageUrl = s3Service.getUrl(valueConfig.getBannerBucket(), pcImage);
        val mobileImageUrl = s3Service.getUrl(valueConfig.getBannerBucket(), mobileImage);
        return BannerImage.builder()
                .pcImageUrl(pcImageUrl)
                .mobileImageUrl(mobileImageUrl)
                .build();
    }

    private Banner saveBanner(Banner banner) {
        return bannerRepository.save(banner);
    }
}
