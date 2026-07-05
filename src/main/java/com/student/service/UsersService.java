package com.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.student.entity.UsersEntity;
import java.util.Map;

/**
 * 管理员服务接口
 */
public interface UsersService extends IService<UsersEntity> {

    /**
     * 管理员登录
     * @return 包含 token 的结果Map
     */
    Map<String, Object> login(String username, String password);
}
