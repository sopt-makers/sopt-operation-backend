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
import org.sopt.makers.operation.banner.domain.*;

import org.sopt.makers.operation.banner.repository.BannerRepository;
import org.sopt.makers.operation.client.s3.S3Service;
import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.config.ValueConfig;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.exception.BannerException;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.web.banner.dto.request.*;
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
    private static final String SLASH = "/";
    private static final String PROTOCOL_SEPARATOR = "//";
    private static final int PROTOCOL_END_OFFSET = 2;
    private static final String S3_BASE_URL = "https://makers-banner.s3.ap-northeast-2.amazonaws.com/";
    private final BannerRepository bannerRepository;
    private final S3Service s3Service;
    private final ValueConfig valueConfig;
    private final Clock clock;

    @Override
    public BannerResponse.BannerDetail getBannerDetail(final long bannerId) {
        val banner = getBannerById(bannerId);
        return BannerResponse.BannerDetail.fromEntity(banner);
    }

    @Transactional
    @Override
    public ResponseEntity<BaseResponse<?>> deleteBanner(final long bannerId) {
        val banner = getBannerById(bannerId);
        bannerRepository.delete(banner);
        return ApiResponseUtil.success(SUCCESS_DELETE_BANNER);
    }

  @Override
  public List<BannerResponse.BannerImageUrl> getExternalBanners(final String imageType, final String location) {
     val publishLocation = PublishLocation.getByValue(location);

     val bannerList = bannerRepository.findBannersByLocation(publishLocation);


      List<String> urlList = bannerList.stream()
              .map(banner -> {
                  return switch (imageType) {
                      case "pc" -> banner.getPcImageUrl();
                      case "mobile" -> banner.getMobileImageUrl();
                      default -> throw new BannerException(NOT_SUPPORTED_PLATFORM_TYPE);
                  };
              })
              .toList();

    return BannerResponse.BannerImageUrl.fromEntity(urlList);
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
        val today = LocalDate.now(clock);
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        val formattedDate = today.format(formatter);

        return location+formattedDate + "_" + contentName + "(" + imageType + ")." + imageExtension;
    }

    @Transactional
    @Override
    public BannerDetail createBanner(BannerRequest.BannerCreateOrModify request) {
        val period = getPublishPeriod(request.start_date(), request.end_date());

        String PcfileName=storeFile(request.image_pc());
        String MobilefileName=storeFile(request.image_mobile());

        val newBanner = Banner.builder()
                .publisher(request.publisher())
                .link(request.link())
                .contentType(ContentType.getByValue(request.content_type()))
                .location(PublishLocation.getByValue(request.location()))
                .period(period)
                .pcImageUrl(S3_BASE_URL+PcfileName)
                .mobileImageUrl(S3_BASE_URL+MobilefileName)
                .build();
        val banner = saveBanner(newBanner);

        return BannerResponse.BannerDetail.fromEntity(banner);
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

        String pcFileName = storeFile(request.image_pc());
        String mobileFileName = storeFile(request.image_mobile());

        // 변경 감지(dirty checking)에 의해 자동으로 업데이트됨
        existingBanner.updateLocation(PublishLocation.getByValue(request.location()));
        existingBanner.updateContentType(ContentType.getByValue(request.content_type()));
        existingBanner.updatePublisher(request.publisher());
        existingBanner.updateLink(request.link());
        existingBanner.updatePeriod(period);
        existingBanner.updatePcImage(S3_BASE_URL + pcFileName);
        existingBanner.updateMobileImage(S3_BASE_URL + mobileFileName);

        return BannerResponse.BannerDetail.fromEntity(existingBanner);
    }

    private void deleteExistImage(String url) {
        val protocolEndIndex = url.indexOf(PROTOCOL_SEPARATOR) + PROTOCOL_END_OFFSET;
        val firstSlashIndex = url.indexOf(SLASH, protocolEndIndex);
        val extractedPath = url.substring(firstSlashIndex);
        s3Service.deleteFile(valueConfig.getBannerBucket(), extractedPath);
    }

    @Override
    public List<BannerSimple> getBanners(FilterCriteria filter, SortCriteria sort) {
        val allBanners = bannerRepository.findAll();
        val filteredBanners = getFilteredBanners(allBanners, filter);
        val resultBanners = getSortedBanners(filteredBanners, sort);
        return resultBanners.stream()
                .map(BannerSimple::fromEntity)
                .toList();
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
                    Comparator.comparing(Banner::getPeriod, (p1, p2) -> p2.getStartDate().compareTo(p1.getStartDate()))
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
