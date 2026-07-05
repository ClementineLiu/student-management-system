package com.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.student.entity.CourseEntity;
import com.student.utils.PageUtils;

import java.util.Map;

public interface CourseService extends IService<CourseEntity> {
    PageUtils queryPage(Map<String, Object> params);
}
