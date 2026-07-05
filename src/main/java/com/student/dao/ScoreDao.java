package com.student.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.entity.ScoreEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScoreDao extends BaseMapper<ScoreEntity> {

    /**
     * 分页查询成绩列表（关联学生、课程、班级）
     */
    @Select("SELECT sc.*, s.name AS studentName, s.student_id AS studentNumber, " +
            "c.course_name AS courseName, ci.class_name AS className " +
            "FROM score sc " +
            "LEFT JOIN student s ON sc.student_id = s.id " +
            "LEFT JOIN course c ON sc.course_id = c.id " +
            "LEFT JOIN class_info ci ON s.class_id = ci.id " +
            "${ew.customSqlSegment}")
    List<ScoreEntity> selectListView(Page<ScoreEntity> page, @Param("ew") Wrapper<ScoreEntity> wrapper);

    /**
     * 成绩区间分布统计
     */
    @Select("SELECT " +
            "SUM(CASE WHEN sc.score < 60 THEN 1 ELSE 0 END) AS range_0_59, " +
            "SUM(CASE WHEN sc.score >= 60 AND sc.score < 70 THEN 1 ELSE 0 END) AS range_60_69, " +
            "SUM(CASE WHEN sc.score >= 70 AND sc.score < 80 THEN 1 ELSE 0 END) AS range_70_79, " +
            "SUM(CASE WHEN sc.score >= 80 AND sc.score < 90 THEN 1 ELSE 0 END) AS range_80_89, " +
            "SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) AS range_90_100 " +
            "FROM score sc")
    Map<String, Object> selectScoreDistribution();
}
