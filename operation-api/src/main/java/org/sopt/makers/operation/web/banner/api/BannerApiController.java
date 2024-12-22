package org.sopt.makers.operation.web.banner.api;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.sopt.makers.operation.dto.BaseResponse;
import org.sopt.makers.operation.util.ApiResponseUtil;
import org.sopt.makers.operation.web.banner.service.BannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.sopt.makers.operation.code.success.web.BannerSuccessCode.SUCCESS_GET_BANNER_DETAIL;

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
    public ResponseEntity<BaseResponse<?>> getPreSignedUrlForBanner(String contentName, String imageType,
                                                                    String imageExtension, String contentType) {
        return null;
    }
}
