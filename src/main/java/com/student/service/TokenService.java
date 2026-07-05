package com.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.student.entity.TokenEntity;

/**
 * Token服务接口
 */
public interface TokenService extends IService<TokenEntity> {

    /**
     * 生成Token（如已有则更新，如没有则新增）
     */
    String generateToken(Long userid, String username, String tableName, String role);

    /**
     * 根据Token字符串查询有效的Token记录
     * @return null表示Token无效或已过期
     */
    TokenEntity getTokenEntity(String token);
}
