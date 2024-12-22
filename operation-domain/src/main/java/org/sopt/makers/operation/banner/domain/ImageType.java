package org.sopt.makers.operation.banner.domain;

import java.util.*;
import lombok.*;
import org.sopt.makers.operation.code.failure.*;
import org.sopt.makers.operation.exception.*;

@RequiredArgsConstructor
@Getter
public enum ImageType {
    PC("pc", "/pc"), MOBILE("mo", "/mobile");

    private final String value;
    private final String location;

    public static ImageType getByValue(String value) {
        return Arrays.stream(ImageType.values())
                .filter(location -> location.getValue().equals(value))
                .findAny().orElseThrow(() -> new BannerException(BannerFailureCode.INVALID_IMAGE_TYPE));
    }
}
