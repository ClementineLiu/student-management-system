package com.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.dao.ScoreDao;
import com.student.entity.ScoreEntity;
import com.student.service.ScoreService;
import com.student.utils.PageUtils;
import com.student.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("scoreService")
public class ScoreServiceImpl extends ServiceImpl<ScoreDao, ScoreEntity> implements ScoreService {

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ScoreEntity> wrapper) {
        Page<ScoreEntity> page = new Query<ScoreEntity>(params).getPage();
        page.setRecords(this.baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }
}
