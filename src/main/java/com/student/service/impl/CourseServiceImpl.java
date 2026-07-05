package com.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.dao.CourseDao;
import com.student.entity.CourseEntity;
import com.student.service.CourseService;
import com.student.utils.PageUtils;
import com.student.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("courseService")
public class CourseServiceImpl extends ServiceImpl<CourseDao, CourseEntity> implements CourseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<CourseEntity> qw = new QueryWrapper<>();
        String courseName = (String) params.get("courseName");
        if (StringUtils.isNotBlank(courseName)) {
            qw.like("course_name", courseName);
        }
        qw.orderByDesc("addtime");

        Page<CourseEntity> page = new Query<CourseEntity>(params).getPage();
        page = this.baseMapper.selectPage(page, qw);
        return new PageUtils(page);
    }
}
