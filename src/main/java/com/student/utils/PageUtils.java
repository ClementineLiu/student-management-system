package com.student.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 */
public class PageUtils implements Serializable {

    private static final long serialVersionUID = 1L;

    private long total;
    private long pageSize;
    private long totalPage;
    private long currPage;
    private List<?> list;

    public PageUtils(Page<?> page) {
        this.list = page.getRecords();
        this.total = page.getTotal();
        this.pageSize = page.getSize();
        this.currPage = page.getCurrent();
        this.totalPage = page.getPages();
    }

    public PageUtils(List<?> list, long total, long pageSize, long currPage) {
        this.list = list;
        this.total = total;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (long) Math.ceil((double) total / pageSize);
    }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public long getPageSize() { return pageSize; }
    public void setPageSize(long pageSize) { this.pageSize = pageSize; }
    public long getTotalPage() { return totalPage; }
    public void setTotalPage(long totalPage) { this.totalPage = totalPage; }
    public long getCurrPage() { return currPage; }
    public void setCurrPage(long currPage) { this.currPage = currPage; }
    public List<?> getList() { return list; }
    public void setList(List<?> list) { this.list = list; }
}
