package org.sopt.makers.operation.web.banner.dto.request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageType {
    PC("pc"), MOBILE("mo");

    private final String type;
}
