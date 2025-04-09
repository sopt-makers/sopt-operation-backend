package org.sopt.makers.operation.web.banner.dto.response;
import lombok.Getter;
import java.util.List;

@Getter
public class BannerPaginationResponse<T> {
    private final Integer limit;
    private final Integer totalCount;
    private final Integer totalPage;
    private final Integer currentPage;
    private final List<T> data;
    private final Boolean hasNextPage;
    private final Boolean hasPrevPage;

    public BannerPaginationResponse(List<T> data, Integer totalCount, Integer limit, int currentPage) {
        this.limit = limit;
        this.totalCount = totalCount;
        this.totalPage = (int) Math.ceil((double) totalCount / limit);
        this.currentPage = currentPage;
        this.data = data;
        this.hasNextPage = this.totalPage > this.currentPage;
        this.hasPrevPage = this.currentPage > 1;
    }
}