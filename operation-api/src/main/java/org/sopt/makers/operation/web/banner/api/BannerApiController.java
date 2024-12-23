package org.sopt.makers.operation.web.banner.api;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.web.banner.dto.request.BannerRequest;
import org.sopt.makers.operation.web.banner.service.BannerService;
import org.springframework.http.ResponseEntity;

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

    @Override
    @GetMapping("/img/pre-signed")
    public ResponseEntity<BaseResponse<?>> getIssuedPreSignedUrlForPutImage(@RequestParam("content-name") String contentName, @RequestParam("image-type") String imageType,
                                                                            @RequestParam("image-extension") String imageExtension, @RequestParam("content-type") String contentType) {
        val response = bannerService.getIssuedPreSignedUrlForPutImage(contentName, imageType, imageExtension, contentType);
        return ApiResponseUtil.success(SUCCESS_GET_BANNER_IMAGE_PRE_SIGNED_URL, response);
    }

    @PostMapping
    @Override
    public ResponseEntity<BaseResponse<?>> createBanner(@RequestBody BannerRequest.BannerCreateOrModify request) {
        val response = bannerService.createBanner(request);
        return ApiResponseUtil.success(SUCCESS_CREATE_BANNER, response);
    }

    @PutMapping("/{bannerId}")
    @Override
    public ResponseEntity<BaseResponse<?>> updateBanner(@PathVariable("bannerId") Long bannerId, BannerRequest.BannerCreateOrModify request) {
        val response = bannerService.updateBanner(bannerId, request);
        return ApiResponseUtil.success(SUCCESS_UPDATE_BANNER, response);
    }
}
