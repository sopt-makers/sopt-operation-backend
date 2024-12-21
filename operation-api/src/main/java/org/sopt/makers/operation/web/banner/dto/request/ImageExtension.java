package org.sopt.makers.operation.web.banner.dto.request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageExtension {
    JPG("jpg"), PNG("png");

    private final String extension;
}
