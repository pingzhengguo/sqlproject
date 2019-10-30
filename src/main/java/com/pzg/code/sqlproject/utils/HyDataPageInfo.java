package com.pzg.code.sqlproject.utils;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Title: HyDataPageInfo
 * 创建文件,实现基本功能
 * =================================================================
 */
@Getter
@Setter
@ToString
public class HyDataPageInfo<T> {
    private List<T> list;
    private InnerPageInfo pageInfo = new InnerPageInfo();

    public static <T> HyDataPageInfo<T> of(PageInfo<T> pageInfo) {
        HyDataPageInfo<T> hyDataPageInfo = new HyDataPageInfo<>();
        hyDataPageInfo.list = pageInfo.getList();
        hyDataPageInfo.pageInfo.currentPage = pageInfo.getPageNum();
        hyDataPageInfo.pageInfo.totalCount = pageInfo.getTotal();
        hyDataPageInfo.pageInfo.pageSize = pageInfo.getSize();
        return hyDataPageInfo;
    }

    @Getter
    @Setter
    @ToString
    public class InnerPageInfo {
        int currentPage;
        long totalCount;
        long pageSize;
    }

}
