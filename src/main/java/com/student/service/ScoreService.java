package com.student.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.student.entity.ScoreEntity;
import com.student.utils.PageUtils;

import java.util.Map;

public interface ScoreService extends IService<ScoreEntity> {
    PageUtils queryPage(Map<String, Object> params, QueryWrapper<ScoreEntity> wrapper);
}
