package com.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.student.entity.ClassInfoEntity;
import com.student.utils.PageUtils;

import java.util.Map;

public interface ClassInfoService extends IService<ClassInfoEntity> {
    PageUtils queryPage(Map<String, Object> params);
}
