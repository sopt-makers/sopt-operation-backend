package org.sopt.makers.operation.web.banner.api;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.code.success.web.BannerSuccessCode;
import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.web.banner.dto.request.BannerRequest;
import org.sopt.makers.operation.web.banner.dto.request.BannerRequest.BannerCreate;
import org.sopt.makers.operation.web.banner.service.BannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_CREATE_BANNER;
import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_DELETE_BANNER;
import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_GET_BANNER_DETAIL;
import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_GET_BANNER_IMAGE_PRE_SIGNED_URL;
import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_GET_EXTERNAL_BANNERS;

import org.springframework.web.bind.annotation.*;

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

  @Override
  @DeleteMapping("/{bannerId}")
  public ResponseEntity<BaseResponse<?>> deleteBanner(
      @PathVariable("bannerId") Long bannerId
  ) {
    bannerService.deleteBanner(bannerId);
    return ApiResponseUtil.success(SUCCESS_DELETE_BANNER);
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

  @PostMapping
  @Override
  public ResponseEntity<BaseResponse<?>> createBanner(
      @RequestBody BannerRequest.BannerCreate request
  ) {
    val response = bannerService.createBanner(request);
    return ApiResponseUtil.success(SUCCESS_CREATE_BANNER, response);
  }
}
