package org.sopt.makers.operation.banner.repository;

import java.util.List;
import org.sopt.makers.operation.banner.domain.Banner;

import org.sopt.makers.operation.banner.domain.PublishLocation;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

  List<Banner> findBannersByLocation(PublishLocation publishLocation);
}
