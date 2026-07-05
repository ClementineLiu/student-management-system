package com.student.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 通用统计 DAO
 */
@Mapper
public interface CommonDao {

    /**
     * 统计聚合：count / sum / avg / max / min
     * @param table 表名
     * @param column 列名
     * @return {count: N} 或 {avg: x.xx} 等
     */
    Map<String, Object> selectCal(@Param("table") String table, @Param("column") String column);

    /**
     * 分组统计：按某列分组，统计每组数量
     * @param table 表名
     * @param column 分组列名
     * @return [{name: 值, value: 数量}, ...]
     */
    List<Map<String, Object>> selectGroup(@Param("table") String table, @Param("column") String column);

    /**
     * 按值统计：按某列分组，计算另一列的聚合值
     * @param table 表名
     * @param xColumn 分组列（X轴）
     * @param yColumn 聚合列（Y轴）
     * @return [{name: 值, value: avg/sum}, ...]
     */
    List<Map<String, Object>> selectValue(@Param("table") String table,
                                           @Param("xColumn") String xColumn,
                                           @Param("yColumn") String yColumn);
}
