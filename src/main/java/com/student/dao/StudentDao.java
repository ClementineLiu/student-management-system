package com.student.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.entity.StudentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentDao extends BaseMapper<StudentEntity> {

    /**
     * 分页查询学生列表（关联班级名称）
     */
    @Select("SELECT s.*, ci.class_name FROM student s " +
            "LEFT JOIN class_info ci ON s.class_id = ci.id " +
            "${ew.customSqlSegment}")
    List<StudentEntity> selectListView(Page<StudentEntity> page, @Param("ew") Wrapper<StudentEntity> wrapper);
}
