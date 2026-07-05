package com.student.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * SQL注入过滤工具类
 */
public class SQLFilter {

    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) return null;
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");
        str = StringUtils.replace(str, "--", "");
        str = StringUtils.replace(str, "/*", "");
        str = StringUtils.replace(str, "*/", "");
        str = StringUtils.replace(str, "%", "");
        str = StringUtils.replace(str, "<", "&lt;");
        str = StringUtils.replace(str, ">", "&gt;");
        return str.trim();
    }
}
