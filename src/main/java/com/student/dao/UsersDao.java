package com.student.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.entity.UsersEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员数据访问层
 */
@Mapper
public interface UsersDao extends BaseMapper<UsersEntity> {
}
