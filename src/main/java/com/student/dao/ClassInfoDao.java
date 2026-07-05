package com.student.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.entity.ClassInfoEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClassInfoDao extends BaseMapper<ClassInfoEntity> {
}
