package com.student.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * 分页查询参数辅助类
 */
public class Query<T> {

    private Page<T> page;

    public Query(Map<String, Object> params) {
        long currPage = 1;
        long limit = 10;

        if (params != null) {
            if (params.containsKey("page")) {
                currPage = parseLong(params.get("page"), 1);
            }
            if (params.containsKey("limit")) {
                limit = parseLong(params.get("limit"), 10);
            }
        }

        if (currPage < 1) currPage = 1;
        if (limit < 1) limit = 10;
        if (limit > 100) limit = 100;

        this.page = new Page<>(currPage, limit);
    }

    private long parseLong(Object obj, long defaultValue) {
        if (obj instanceof Number) return ((Number) obj).longValue();
        if (obj instanceof String) {
            try { return Long.parseLong((String) obj); }
            catch (NumberFormatException ignored) {}
        }
        return defaultValue;
    }

    public Page<T> getPage() { return page; }
}
