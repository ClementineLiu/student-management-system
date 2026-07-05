package com.student.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Map;

/**
 * MyBatis-Plus 条件构建工具类
 */
public class MPUtil {

    public static <T> QueryWrapper<T> allEq(Map<String, Object> params, Class<T> clazz) {
        QueryWrapper<T> qw = new QueryWrapper<>();
        if (params == null) return qw;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals("page") || key.equals("limit") || key.equals("sort") || key.equals("order")) continue;
            if (value != null && !value.toString().trim().isEmpty()) {
                qw.eq(key, value);
            }
        }
        return qw;
    }
}
