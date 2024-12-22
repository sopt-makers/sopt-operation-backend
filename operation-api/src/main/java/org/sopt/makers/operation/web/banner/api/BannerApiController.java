package org.sopt.makers.operation.web.banner.api;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.web.banner.dto.request.BannerRequest;
import org.sopt.makers.operation.web.banner.service.BannerService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_CREATE_BANNER;
import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_GET_BANNER_DETAIL;
import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_GET_BANNER_IMAGE_PRE_SIGNED_URL;

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
    @GetMapping("/img/pre-signed")
    public ResponseEntity<BaseResponse<?>> getPreSignedUrlForBanner(@RequestParam("content-name") String contentName, @RequestParam("image-type") String imageType,
                                                                    @RequestParam("image-extension") String imageExtension, @RequestParam("content-type") String contentType) {
        val response = bannerService.getPutPreSignedUrlForBanner(contentName, imageType, imageExtension, contentType);
        return ApiResponseUtil.success(SUCCESS_GET_BANNER_IMAGE_PRE_SIGNED_URL, response);
    }

    @Override
    public ResponseEntity<BaseResponse<?>> createBanner(@RequestBody BannerRequest.BannerCreate request) {
        val response = bannerService.createBanner(request);
        return ApiResponseUtil.success(SUCCESS_CREATE_BANNER, response);
    }
}
