package org.sopt.makers.operation.banner.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageType {
    PC("pc", "/pc"), MOBILE("mo", "mobile");

    private final String type;
    private final String location;
}
