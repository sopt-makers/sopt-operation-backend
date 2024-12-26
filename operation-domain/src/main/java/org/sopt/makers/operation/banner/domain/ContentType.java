package org.sopt.makers.operation.banner.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.exception.BannerException;

import java.util.Arrays;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum ContentType {
    PRODUCT("product", "product/"),
    BIRTHDAY("birthday", "birthday/"),
    SPONSOR("sponsor", "sponsor/"),
    EVENT("event", "event/"),
    ETC("etc", "etc/"),
    ;

    private final String value;
    private final String location;

    public static ContentType getByValue(String value) {
        return Arrays.stream(ContentType.values())
                .filter(location -> location.getValue().equals(value))
                .findAny().orElseThrow(() -> new BannerException(BannerFailureCode.NOT_FOUND_CONTENT_TYPE));
    }
}
