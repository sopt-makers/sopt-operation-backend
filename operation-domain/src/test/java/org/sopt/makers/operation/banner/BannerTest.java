//package org.sopt.makers.operation.banner;
//
//import org.sopt.makers.operation.DatabaseCleaner;
//
//import org.sopt.makers.operation.banner.domain.Banner;
//import org.sopt.makers.operation.banner.domain.ContentType;
//import org.sopt.makers.operation.banner.domain.PublishLocation;
//import org.sopt.makers.operation.banner.domain.BannerImage;
//import org.sopt.makers.operation.banner.domain.PublishPeriod;
//import org.sopt.makers.operation.banner.repository.BannerRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.TestInstance;
//
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@DisplayName("[[ Unit Test (Domain) ]] - Banner")
//@EntityScan(basePackages = "org.sopt.makers.operation.banner")
//@ContextConfiguration(classes = {
//        Banner.class, BannerImage.class, PublishPeriod.class,
//        BannerRepository.class, DatabaseCleaner.class
//})
//@EnableAutoConfiguration
//@ActiveProfiles("test")
//public class BannerTest {
//    private static final long TEST_BANNER_ID = 1L;
//    private static final PublishLocation TEST_BANNER_LOCATION = PublishLocation.PLAYGROUND_COMMUNITY;
//    private static final ContentType TEST_BANNER_CONTENT_TYPE = ContentType.PRODUCT;
//    private static final String TEST_BANNER_PUBLISHER = "PUBLISHER";
//    private static final LocalDate TEST_BANNER_START_DATE = LocalDate.of(2024, 1, 1);
//    private static final LocalDate TEST_BANNER_END_DATE = LocalDate.of(2024, 12, 31);
//    private static final String TEST_BANNER_PC_IMAGE_URL = "image-url-for-pc";
//    private static final String TEST_BANNER_MOBILE_IMAGE_URL = "image-url-for-mobile";
//    private static final PublishPeriod TEST_PUBLISH_PERIOD = PublishPeriod.builder()
//            .startDate(TEST_BANNER_START_DATE)
//            .endDate(TEST_BANNER_END_DATE).build();
//    private static final BannerImage TEST_BANNER_IMAGE = BannerImage.builder()
//            .pcImageUrl(TEST_BANNER_PC_IMAGE_URL)
//            .mobileImageUrl(TEST_BANNER_MOBILE_IMAGE_URL).build();
//
//    private static final Banner TEST_BANNER = Banner.builder()
//            .location(TEST_BANNER_LOCATION)
//            .contentType(TEST_BANNER_CONTENT_TYPE)
//            .publisher(TEST_BANNER_PUBLISHER)
//            .period(TEST_PUBLISH_PERIOD)
//            .image(TEST_BANNER_IMAGE)
//            .build();
//
//    @Autowired
//    private BannerRepository bannerRepository;
//
//    @Autowired
//    private DatabaseCleaner cleaner;
//
//    @Nested
//    @DisplayName("[TEST] 단일 배너 수정 시나리오")
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    class UpdateTest{
//
//        @BeforeEach
//        void initData() {
//            cleaner.execute();
//            bannerRepository.save(TEST_BANNER);
//        }
//
//        @Test
//        @DisplayName("게시 기간 중 시작 일자를 정상적으로 변경한다.")
//        @Rollback(value = false)
//        void updatePeriodStartDate(){
//            // given
//            Banner givenBanner = bannerRepository.findById(TEST_BANNER_ID).get();
//            LocalDate givenUpdateStartDate = LocalDate.of(2024, 6, 30);
//
//            // when
//            givenBanner.getPeriod().updateStartDate(givenUpdateStartDate);
//            bannerRepository.save(givenBanner);
//
//            // then
//            Banner resultBanner = bannerRepository.findById(TEST_BANNER_ID).get();
//            LocalDate resultBannerStartDate = resultBanner.getPeriod().getStartDate();
//            assertThat(resultBannerStartDate).isEqualTo(givenUpdateStartDate);
//        }
//
//        @Test
//        @DisplayName("게시 기간 중 종료 일자를 정상적으로 변경한다.")
//        @Rollback(value = false)
//        void updatePeriodEndDate(){
//            // given
//            Banner givenBanner = bannerRepository.findById(TEST_BANNER_ID).get();
//            LocalDate givenUpdateEndDate = LocalDate.of(2030, 12, 31);
//
//            // when
//            givenBanner.getPeriod().updateEndDate(givenUpdateEndDate);
//            bannerRepository.save(givenBanner);
//
//            // then
//            Banner resultBanner = bannerRepository.findById(TEST_BANNER_ID).get();
//            assertThat(resultBanner.getPeriod().getEndDate()).isEqualTo(givenUpdateEndDate);
//        }
//
//        @Test
//        @DisplayName("게시 이미지 중 PC 사진 URL을 정상적으로 변경한다.")
//        @Rollback(value = false)
//        void updateImageForWeb(){
//            // given
//            Banner givenBanner = bannerRepository.findById(TEST_BANNER_ID).get();
//            String givenUpdateImageForWeb = "image-url-web-update";
//
//            // when
//            givenBanner.getImage().updatePcImage(givenUpdateImageForWeb);
//            bannerRepository.save(givenBanner);
//
//            // then
//            Banner resultBanner = bannerRepository.findById(TEST_BANNER_ID).get();
//            assertThat(resultBanner.getImage().getPcImageUrl()).isEqualTo(givenUpdateImageForWeb);
//        }
//
//        @Test
//        @DisplayName("게시 이미지 중 Mobile 사진 URL을 정상적으로 변경한다.")
//        @Rollback(value = false)
//        void updateImageForMobile(){
//            // given
//            Banner givenBanner = bannerRepository.findById(TEST_BANNER_ID).get();
//            String givenUpdateImageForMobile = "image-url-mobile-update";
//
//            // when
//            givenBanner.getImage().updateMobileImage(givenUpdateImageForMobile);
//            bannerRepository.save(givenBanner);
//
//            // then
//            Banner resultBanner = bannerRepository.findById(TEST_BANNER_ID).get();
//            assertThat(resultBanner.getImage().getMobileImageUrl()).isEqualTo(givenUpdateImageForMobile);
//        }
//    }
//
//    @Nested
//    @DisplayName("[TEST] 단일 배너 조회 시나리오")
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    class SelectTest{
//
//        @BeforeAll
//        void initDatabase() {
//            cleaner.execute();
//            bannerRepository.save(TEST_BANNER);
//        }
//
//        @Test
//        @DisplayName("게시 기간을 정상적으로 조회한다.")
//        void getPeriod() {
//             // given
//             Banner givenBanner = bannerRepository.findById(TEST_BANNER_ID).get();
//
//             // when
//            PublishPeriod resultPeriod = givenBanner.getPeriod();
//
//            // then
//            assertThat(resultPeriod.getStartDate()).isEqualTo(TEST_BANNER_START_DATE);
//            assertThat(resultPeriod.getEndDate()).isEqualTo(TEST_BANNER_END_DATE);
//        }
//
//        @Test
//        @DisplayName("게시 이미지들을 정상적으로 조회한다.")
//        void getImage() {
//            // given
//            Banner givenBanner = bannerRepository.findById(TEST_BANNER_ID).get();
//
//            // when
//            BannerImage resultImage = givenBanner.getImage();
//
//            // then
//            assertThat(resultImage.getPcImageUrl()).isEqualTo(TEST_BANNER_PC_IMAGE_URL);
//            assertThat(resultImage.getMobileImageUrl()).isEqualTo(TEST_BANNER_MOBILE_IMAGE_URL);
//        }
//
//        @Test
//        @DisplayName("게시 위치를 정상적으로 조회한다.")
//        void getLocation() {
//            // given
//            Banner givenBanner = bannerRepository.findById(TEST_BANNER_ID).get();
//
//            // when
//            PublishLocation resultLocation = givenBanner.getLocation();
//
//            // then
//            assertThat(resultLocation).isEqualTo(TEST_BANNER_LOCATION);
//        }
//    }
//
//}
