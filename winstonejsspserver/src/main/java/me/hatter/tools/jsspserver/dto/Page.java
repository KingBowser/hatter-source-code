package me.hatter.tools.jsspserver.dto;

public class Page {

    private Integer pageSize  = 10;
    private Integer pageIndex = 0;
    private Integer totalCount;

    public Integer getPageSize() {
        if (pageSize == null || pageSize < 1) return 1;
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        if (pageIndex == null || pageIndex < 0) return 0;
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getTotalCount() {
        if (totalCount == null || totalCount < 0) return 0;
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
