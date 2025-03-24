package org.sopt.makers.operation.web.banner.api;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.web.banner.dto.request.BannerRequest;
import org.sopt.makers.operation.web.banner.service.BannerService;
import org.sopt.makers.operation.web.banner.service.BannerService.FilterCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.*;
import static org.sopt.makers.operation.web.banner.service.BannerService.*;

import org.springframework.web.bind.annotation.*;

import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_CREATE_BANNER;
import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_GET_BANNER_DETAIL;
import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_GET_BANNER_IMAGE_PRE_SIGNED_URL;
import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_UPDATE_BANNER;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerApiController implements BannerApi {

  private final BannerService bannerService;

  @Override
  @GetMapping("/{bannerId}")
  public ResponseEntity<BaseResponse<?>> getBannerDetail(
      @PathVariable("bannerId") Long bannerId
  ) {
    val response = bannerService.getBannerDetail(bannerId);
    return ApiResponseUtil.success(SUCCESS_GET_BANNER_DETAIL, response);
  }

  @PostMapping
  @Override
  public ResponseEntity<BaseResponse<?>> createBanner(@ModelAttribute  BannerRequest.BannerCreateOrModify request) {
      val response = bannerService.createBanner(request);
      return ApiResponseUtil.success(SUCCESS_CREATE_BANNER, response);
  }

  @PutMapping("/{bannerId}")
  @Override
  public ResponseEntity<BaseResponse<?>> updateBanner(@PathVariable("bannerId") Long bannerId, @ModelAttribute  BannerRequest.BannerCreateOrModify request) {
      val response = bannerService.updateBanner(bannerId, request);
      return ApiResponseUtil.success(SUCCESS_UPDATE_BANNER, response);
  }

  @Override
  @GetMapping
  public ResponseEntity<BaseResponse<?>> getBanners(
          @RequestParam(value = "filter", required = false, defaultValue = "all") String filterCriteriaParameter,
          @RequestParam(value = "sort", required = false, defaultValue = "status") String sortCriteriaParameter
  ) {
    val progressStatus = FilterCriteria.fromParameter(filterCriteriaParameter);
    val sortCriteria = SortCriteria.fromParameter(sortCriteriaParameter);

    val response = bannerService.getBanners(progressStatus, sortCriteria);
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
      @RequestParam("image_type") String imageType,
      @RequestParam("location") String location
  ) {
    return ApiResponseUtil.success(SUCCESS_GET_EXTERNAL_BANNERS,
        bannerService.getExternalBanners(imageType, location));
  }

  @Override
  @GetMapping("/img/pre-signed")
  public ResponseEntity<BaseResponse<?>> getIssuedPreSignedUrlForPutImage(
      @RequestParam("content_name") String contentName,
      @RequestParam("image_type") String imageType,
      @RequestParam("image_extension") String imageExtension,
      @RequestParam("content_type") String contentType
  ) {
      val response = bannerService.getIssuedPreSignedUrlForPutImage(contentName, imageType,
        imageExtension, contentType);
    return ApiResponseUtil.success(SUCCESS_GET_BANNER_IMAGE_PRE_SIGNED_URL, response);
  }
}
