package org.sopt.makers.operation.web.banner.service;

import static org.sopt.makers.operation.code.failure.BannerFailureCode.NOT_SUPPORTED_PLATFORM_TYPE;
import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_DELETE_BANNER;

import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.banner.domain.Banner;
import org.sopt.makers.operation.banner.domain.PublishLocation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import org.sopt.makers.operation.banner.domain.*;

import org.sopt.makers.operation.banner.repository.BannerRepository;
import org.sopt.makers.operation.client.s3.S3Service;
import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.exception.BannerException;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.web.banner.dto.request.*;
import org.sopt.makers.operation.web.banner.dto.response.BannerPaginationResponse;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final S3Service s3Service;
    private final ValueConfig valueConfig;
    private final Clock clock;

    @Override
    public BannerResponse.BannerDetail getBannerDetail(final long bannerId) {
        val banner = getBannerById(bannerId);
        String pcSignedUrl = s3Service.getUrl(valueConfig.getBannerBucket(), banner.getPcImageKey());
        String mobileSignedUrl = s3Service.getUrl(valueConfig.getBannerBucket(), banner.getMobileImageKey());

        return BannerResponse.BannerDetail.fromEntity(banner, pcSignedUrl, mobileSignedUrl);
    }

    @Transactional
    @Override
    public ResponseEntity<BaseResponse<?>> deleteBanner(final long bannerId) {
        val banner = getBannerById(bannerId);
        val currentDate = LocalDate.now(clock);
        val bannerStatus = banner.getPeriod().getPublishStatus(currentDate);

        if (PublishStatus.DONE.equals(bannerStatus)) {
            throw new BannerException(BannerFailureCode.CANNOT_DELETE_DONE_BANNER);
        }

        if (banner.getPcImageKey() != null) {
            s3Service.deleteFile(valueConfig.getBannerBucket(), banner.getPcImageKey());
        }

        if (banner.getMobileImageKey() != null) {
            s3Service.deleteFile(valueConfig.getBannerBucket(), banner.getMobileImageKey());
        }

        bannerRepository.delete(banner);
        return ApiResponseUtil.success(SUCCESS_DELETE_BANNER);
    }


    @Override
    public List<BannerResponse.BannerImageWithBothPlatforms> getExternalBanners(final String location) {
        val publishLocation = PublishLocation.getByValue(location);
        val bannerList = bannerRepository.findBannersByLocation(publishLocation);

        val currentDate = LocalDate.now();

        List<Banner> activeBanners = bannerList.stream()
                .filter(banner -> {
                    val publishStatus = banner.getPeriod().getPublishStatus(currentDate);
                    return publishStatus == PublishStatus.IN_PROGRESS;
                })
                .toList();

        // PC URL 생성 함수
        Function<Banner, String> pcUrlExtractor = banner ->
                s3Service.getUrl(valueConfig.getBannerBucket(), banner.getPcImageKey());

        // Mobile URL 생성 함수
        Function<Banner, String> mobileUrlExtractor = banner ->
                s3Service.getUrl(valueConfig.getBannerBucket(), banner.getMobileImageKey());

        return BannerResponse.BannerImageWithBothPlatforms.fromEntities(activeBanners, pcUrlExtractor, mobileUrlExtractor);
    }

  private Banner getBannerById(final long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new BannerException(BannerFailureCode.NOT_FOUND_BANNER));
    }

    private String getBannerImageName(String location, String contentName, String imageType, String imageExtension) {
        val today = LocalDate.now(clock);
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        val formattedDate = today.format(formatter);

        return location+formattedDate + "_" + contentName + "(" + imageType + ")." + imageExtension;
    }

    @Transactional
    @Override
    public BannerDetail createBanner(BannerRequest.BannerCreateOrModify request) {
        val period = getPublishPeriod(request.start_date(), request.end_date());

        String pcImageKey = storeFile(request.image_pc());
        String mobileImageKey = storeFile(request.image_mobile());

        val newBanner = Banner.builder()
                .publisher(request.publisher())
                .link(request.link())
                .contentType(ContentType.getByValue(request.content_type()))
                .location(PublishLocation.getByValue(request.location()))
                .period(period)
                .pcImageKey(pcImageKey)
                .mobileImageKey(mobileImageKey)
                .build();
        val banner = saveBanner(newBanner);

        String pcSignedUrl = s3Service.getUrl(valueConfig.getBannerBucket(), pcImageKey);
        String mobileSignedUrl = s3Service.getUrl(valueConfig.getBannerBucket(), mobileImageKey);

        return BannerResponse.BannerDetail.fromEntity(banner, pcSignedUrl, mobileSignedUrl);
    }


    private String storeFile(MultipartFile file) {
        try {
            return s3Service.uploadImage("banners-images/", file,valueConfig.getBannerBucket());
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }

    @Transactional
    @Override
    public BannerDetail updateBanner(Long bannerId, BannerRequest.BannerCreateOrModify request) {
        PublishPeriod period = getPublishPeriod(request.start_date(), request.end_date());
        Banner existingBanner = getBannerById(bannerId);
        val currentDate = LocalDate.now(clock);
        val bannerStatus = existingBanner.getPeriod().getPublishStatus(currentDate);

        if (PublishStatus.DONE.equals(bannerStatus)) {
            throw new BannerException(BannerFailureCode.CANNOT_MODIFY_DONE_BANNER);
        }


        String oldPcImageKey = existingBanner.getPcImageKey();
        String oldMobileImageKey = existingBanner.getMobileImageKey();

        String pcImageKey = storeFile(request.image_pc());
        String mobileImageKey = storeFile(request.image_mobile());

        // 변경 감지(dirty checking)에 의해 자동으로 업데이트됨
        existingBanner.updateLocation(PublishLocation.getByValue(request.location()));
        existingBanner.updateContentType(ContentType.getByValue(request.content_type()));
        existingBanner.updatePublisher(request.publisher());
        existingBanner.updateLink(request.link());
        existingBanner.updatePeriod(period);
        existingBanner.updatePcImageKey(pcImageKey);
        existingBanner.updateMobileImageKey(mobileImageKey);

        if (oldPcImageKey != null) {
            s3Service.deleteFile(valueConfig.getBannerBucket(), oldPcImageKey);
        }

        if (oldMobileImageKey != null) {
            s3Service.deleteFile(valueConfig.getBannerBucket(), oldMobileImageKey);
        }

        String pcSignedUrl = s3Service.getUrl(valueConfig.getBannerBucket(), pcImageKey);
        String mobileSignedUrl = s3Service.getUrl(valueConfig.getBannerBucket(), mobileImageKey);

        return BannerResponse.BannerDetail.fromEntity(existingBanner, pcSignedUrl, mobileSignedUrl);
    }

    @Override
    public BannerPaginationResponse<BannerSimple> getBannersWithPagination(
            FilterCriteria filter,
            SortCriteria sort,
            int page,
            int limit) {
        val allBanners = bannerRepository.findAll();

        val filteredBanners = getFilteredBanners(allBanners, filter);
        val sortedBanners = getSortedBanners(filteredBanners, sort);

        val totalCount = sortedBanners.size();
        val startIndex = (page - 1) * limit;
        val endIndex = Math.min(startIndex + limit, totalCount);

        if (startIndex > totalCount) {
            return new BannerPaginationResponse<>(List.of(), totalCount, limit, page);
        }

        val paginatedBanners = sortedBanners.subList(startIndex, endIndex)
                .stream()
                .map(BannerSimple::fromEntity)
                .toList();

        return new BannerPaginationResponse<>(
                paginatedBanners,
                totalCount,
                limit,
                page
        );
    }

    private List<Banner> getFilteredBanners(List<Banner> banners, FilterCriteria filter) {
        if (FilterCriteria.ALL.equals(filter)) {
            return banners;
        }
        val targetStatus = PublishStatus.getByValue(filter.getParameter());
        return banners.stream()
                .filter(banner -> targetStatus.equals(banner.getPeriod().getPublishStatus(LocalDate.now(clock))))
                .toList();
    }

    private List<Banner> getSortedBanners(List<Banner> banners, SortCriteria criteria) {
        return switch (criteria) {
            case STATUS, START_DATE -> banners.stream().sorted(
                    Comparator.comparing(Banner::getPeriod, Comparator.comparing(PublishPeriod::getStartDate))
                            .thenComparing(Banner::getPeriod, Comparator.comparing(PublishPeriod::getEndDate))
            ).toList();
            case END_DATE -> banners.stream().sorted(
                    Comparator.comparing(Banner::getPeriod, Comparator.comparing(PublishPeriod::getEndDate))
                            .thenComparing(Banner::getPeriod, (p1, p2) -> p2.getStartDate().compareTo(p1.getStartDate()))
            ).toList();
        };
    }

    private PublishPeriod getPublishPeriod(LocalDate startDate, LocalDate endDate) {
        return PublishPeriod.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    private Banner saveBanner(Banner banner) {
        return bannerRepository.save(banner);
    }
}
