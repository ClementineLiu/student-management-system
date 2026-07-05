package com.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.dao.StudentDao;
import com.student.entity.StudentEntity;
import com.student.service.StudentService;
import com.student.utils.PageUtils;
import com.student.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("studentService")
public class StudentServiceImpl extends ServiceImpl<StudentDao, StudentEntity> implements StudentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<StudentEntity> wrapper) {
        Page<StudentEntity> page = new Query<StudentEntity>(params).getPage();
        page.setRecords(this.baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }
}
