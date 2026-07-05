package com.student.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.student.entity.StudentEntity;
import com.student.utils.PageUtils;

import java.util.Map;

public interface StudentService extends IService<StudentEntity> {
    PageUtils queryPage(Map<String, Object> params, QueryWrapper<StudentEntity> wrapper);
}
