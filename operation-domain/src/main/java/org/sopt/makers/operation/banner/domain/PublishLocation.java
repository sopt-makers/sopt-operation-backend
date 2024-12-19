package org.sopt.makers.operation.banner.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.exception.BannerException;

import java.util.Arrays;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum PublishLocation {
    PLAYGROUND_COMMUNITY("pg_community"),
    CREW_MAIN("cr_main"),
    CREW_FEED("cr_feed"),
    OFFICIAL_PAGE("org"),
    ;

    private final String value;

    public static PublishLocation getByValue(String value) {
        return Arrays.stream(PublishLocation.values())
                .filter(location -> location.getValue().equals(value))
                .findAny().orElseThrow(() -> new BannerException(BannerFailureCode.NOT_FOUND_LOCATION));
    }
}
