package com.student.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.entity.TokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Token数据访问层
 */
@Mapper
public interface TokenDao extends BaseMapper<TokenEntity> {
}
