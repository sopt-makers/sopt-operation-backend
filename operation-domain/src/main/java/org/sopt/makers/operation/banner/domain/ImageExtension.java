package org.sopt.makers.operation.banner.domain;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.exception.BannerException;

@RequiredArgsConstructor
@Getter
public enum ImageExtension {
    PNG("png");

    private final String value;

    public static ImageExtension getByValue(String value) {
        return Arrays.stream(ImageExtension.values())
                .filter(location -> location.getValue().equals(value))
                .findAny().orElseThrow(() -> new BannerException(BannerFailureCode.INVALID_IMAGE_TYPE));
    }
}
