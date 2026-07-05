package com.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.dao.ClassInfoDao;
import com.student.entity.ClassInfoEntity;
import com.student.service.ClassInfoService;
import com.student.utils.PageUtils;
import com.student.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("classInfoService")
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoDao, ClassInfoEntity> implements ClassInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<ClassInfoEntity> qw = new QueryWrapper<>();
        String className = (String) params.get("className");
        if (StringUtils.isNotBlank(className)) {
            qw.like("class_name", className);
        }
        qw.orderByDesc("addtime");

        Page<ClassInfoEntity> page = new Query<ClassInfoEntity>(params).getPage();
        page = this.baseMapper.selectPage(page, qw);
        return new PageUtils(page);
    }
}
