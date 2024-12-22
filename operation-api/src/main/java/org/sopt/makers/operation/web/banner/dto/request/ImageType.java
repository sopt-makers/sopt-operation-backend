package org.sopt.makers.operation.web.banner.dto.request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageType {
    PC("pc", "/pc"), MOBILE("mo", "mobile");

    private final String type;
    private final String location;
}
