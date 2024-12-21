package org.sopt.makers.operation.web.banner.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.makers.operation.banner.domain.Banner;
import org.sopt.makers.operation.banner.domain.PublishLocation;
import org.sopt.makers.operation.banner.repository.BannerRepository;
import org.sopt.makers.operation.code.failure.BannerFailureCode;
import org.sopt.makers.operation.exception.BannerException;
import org.sopt.makers.operation.web.banner.dto.response.BannerResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    @Override
    public BannerResponse.BannerDetail getBannerDetail(final long bannerId) {
        val banner = getBannerById(bannerId);
        return BannerResponse.BannerDetail.fromEntity(banner);
    }

    @Override
    public void deleteBanner(final long bannerId) {
        val banner = getBannerById(bannerId);
        bannerRepository.delete(banner);
    }

  @Override
  public List<BannerResponse.BannerImageUrl> getExternalBanners(final String platform, final String location) {
     PublishLocation publishLocation = PublishLocation.getByValue(location);

     val bannerList = bannerRepository.findBannersByLocation(publishLocation);

     List<String> list = bannerList.stream()
         .map( banner -> banner.getImage().retrieveImageUrl(platform))
         .collect(Collectors.toUnmodifiableList());

    return BannerResponse.BannerImageUrl.fromEntity(list);
  }

  private Banner getBannerById(final long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new BannerException(BannerFailureCode.NOT_FOUNT_BANNER));
    }
}
