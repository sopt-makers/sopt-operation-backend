package org.sopt.makers.operation.web.banner.api;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.code.failure.ApiKeyFailureCode;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.exception.ApiKeyException;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.web.banner.dto.request.BannerRequest;
import org.sopt.makers.operation.web.banner.service.BannerService;
import org.sopt.makers.operation.web.banner.service.BannerService.FilterCriteria;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.*;
import static org.sopt.makers.operation.web.banner.service.BannerService.*;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_CREATE_BANNER;
import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_GET_BANNER_DETAIL;
import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_UPDATE_BANNER;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerApiController implements BannerApi {

  private final BannerService bannerService;
  @Value("${official.apikey}")
  private String apiKey;

  @Override
  @GetMapping("/{bannerId}")
  public ResponseEntity<BaseResponse<?>> getBannerDetail(
      @PathVariable("bannerId") Long bannerId
  ) {
    val response = bannerService.getBannerDetail(bannerId);
    return ApiResponseUtil.success(SUCCESS_GET_BANNER_DETAIL, response);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Override
  public ResponseEntity<BaseResponse<?>> createBanner(
          @RequestPart("location") String location,
          @RequestPart("content_type") String contentType,
          @RequestPart("publisher") String publisher,
          @RequestPart("start_date") String startDate,
          @RequestPart("end_date") String endDate,
          @RequestPart(name = "link", required = false) String link,
          @RequestPart("image_pc") MultipartFile imagePc,
          @RequestPart("image_mobile") MultipartFile imageMobile
  ) {
      val request = new BannerRequest.BannerCreateOrModify(
              location, contentType, publisher, startDate, endDate, link, imagePc, imageMobile
      );
      val response = bannerService.createBanner(request);
      return ApiResponseUtil.success(SUCCESS_CREATE_BANNER, response);
  }

  @PutMapping(value = "/{bannerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Override
  public ResponseEntity<BaseResponse<?>> updateBanner(
          @PathVariable("bannerId") Long bannerId,
          @RequestPart("location") String location,
          @RequestPart("content_type") String contentType,
          @RequestPart("publisher") String publisher,
          @RequestPart("start_date") String startDate,
          @RequestPart("end_date") String endDate,
          @RequestPart(name = "link", required = false) String link,
          @RequestPart("image_pc") MultipartFile imagePc,
          @RequestPart("image_mobile") MultipartFile imageMobile
  ) {
      val request = new BannerRequest.BannerCreateOrModify(
              location, contentType, publisher, startDate, endDate, link, imagePc, imageMobile
      );
      val response = bannerService.updateBanner(bannerId, request);
      return ApiResponseUtil.success(SUCCESS_UPDATE_BANNER, response);
  }

  @Override
  @GetMapping
  public ResponseEntity<BaseResponse<?>> getBanners(
          @RequestParam(value = "status", required = false, defaultValue = "all") String filterCriteriaParameter,
          @RequestParam(value = "sort", required = false, defaultValue = "status") String sortCriteriaParameter,
          @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
          @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit
  ) {
    val progressStatus = FilterCriteria.fromParameter(filterCriteriaParameter);
    val sortCriteria = SortCriteria.fromParameter(sortCriteriaParameter);

    // 유효성 검사 및 기본값 적용
    int pageValue = (page != null && page > 0) ? page : 1;
    int limitValue = (limit != null && limit > 0) ? limit : 20;

    val response = bannerService.getBannersWithPagination(
            progressStatus,
            sortCriteria,
            pageValue,
            limitValue
    );

    return ApiResponseUtil.success(SUCCESS_GET_BANNER_LIST, response);
  }

  @Override
  @DeleteMapping("/{bannerId}")
  public ResponseEntity<BaseResponse<?>> deleteBanner(
      @PathVariable("bannerId") Long bannerId
  ) {
    return bannerService.deleteBanner(bannerId);
  }

  @Override
  @GetMapping("/images")
  public ResponseEntity<BaseResponse<?>> getExternalBanners(
          @RequestParam("location") String location,
          @RequestHeader("api-key") String apiKey
  ) {


    if (apiKey == null || apiKey.trim().isEmpty()) {
      throw new ApiKeyException(ApiKeyFailureCode.MISSING_API_KEY);
    }

    if (!this.apiKey.equals(apiKey)) {
      throw new ApiKeyException(ApiKeyFailureCode.INVALID_API_KEY);
    }

    val result = bannerService.getExternalBanners(location);
    return ApiResponseUtil.success(SUCCESS_GET_EXTERNAL_BANNERS, result);
  }


}
