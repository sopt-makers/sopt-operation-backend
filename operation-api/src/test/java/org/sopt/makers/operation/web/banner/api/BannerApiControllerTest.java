package org.sopt.makers.operation.web.banner.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.sopt.makers.operation.code.success.web.BannerSuccessCode;
import org.sopt.makers.operation.filter.JwtAuthenticationFilter;
import org.sopt.makers.operation.filter.JwtExceptionFilter;
import org.sopt.makers.operation.jwt.JwtTokenProvider;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;
import org.sopt.makers.operation.web.banner.service.BannerService;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {BannerApiController.class},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtAuthenticationFilter.class, JwtExceptionFilter.class})},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, JwtTokenProvider.class}
)
@DisplayName("[Web Layer Test] Banner API Controller")
class BannerApiControllerTest {
    private static final long MOCK_BANNER_ID = 1L;

    @MockBean
    private BannerService bannerService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setMockBanner() {
        BannerResponse.BannerDetail mockBannerDetail = new BannerResponse.BannerDetail(
                MOCK_BANNER_ID, "in_progress", "pg_community", "product", "publisher", "link",
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), "image-url-pc", "image-url-mobile"
        );

        when(bannerService.getBannerDetail(MOCK_BANNER_ID))
                .thenReturn(mockBannerDetail);
    }


    @Test
    @DisplayName("(GET) Banner Detail")
    void getBannerDetail() throws Exception {
        // given
        BannerResponse.BannerDetail givenBannerDetail = bannerService.getBannerDetail(MOCK_BANNER_ID);

        this.mockMvc.perform(
                        // when
                        get("/api/v1/banners/" + MOCK_BANNER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mock(Principal.class)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value(BannerSuccessCode.SUCCESS_GET_BANNER_DETAIL.getMessage()))
                .andExpect(jsonPath("$.data.id").value(givenBannerDetail.bannerId()))
                .andExpect(jsonPath("$.data.status").value(givenBannerDetail.bannerStatus()))
                .andExpect(jsonPath("$.data.location").value(givenBannerDetail.bannerLocation()))
                .andExpect(jsonPath("$.data.content_type").value(givenBannerDetail.bannerType()))
                .andExpect(jsonPath("$.data.publisher").value(givenBannerDetail.publisher()))
                .andExpect(jsonPath("$.data.link").value(givenBannerDetail.link()))
                .andExpect(jsonPath("$.data.start_date").value(givenBannerDetail.startDate().toString()))
                .andExpect(jsonPath("$.data.end_date").value(givenBannerDetail.endDate().toString()))
                .andExpect(jsonPath("$.data.image_url_pc").value(givenBannerDetail.pcImageUrl()))
                .andExpect(jsonPath("$.data.image_url_mobile").value(givenBannerDetail.mobileImageUrl()));
    }

    @Test
    @DisplayName("(DELETE) Banner Delete")
    void deleteBanner() throws Exception {
        //given
        BannerResponse.BannerDetail mockBannerDetail =  bannerService.getBannerDetail(MOCK_BANNER_ID);

        this.mockMvc.perform(
            //when
            delete("/api/v1/banners/" + MOCK_BANNER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mock(Principal.class)))

            //then
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success").value("true"));

    }

    @Test
    @DisplayName("(GET) External Banners")
    void getExternalBanners() throws Exception {
        // given
        String imageType = "pc";
        String location = "pg_community";

        this.mockMvc.perform(
                        // when
                        get("/api/v1/banners/images")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("platform", imageType)
                                .param("location", location)
                                .principal(mock(Principal.class)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"));
    }
}
