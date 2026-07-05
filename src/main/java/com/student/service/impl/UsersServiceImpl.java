package com.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.dao.UsersDao;
import com.student.entity.UsersEntity;
import com.student.service.TokenService;
import com.student.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员服务实现
 */
@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersDao, UsersEntity> implements UsersService {

    @Autowired
    private TokenService tokenService;

    @Override
    public Map<String, Object> login(String username, String password) {
        // 查询用户
        QueryWrapper<UsersEntity> qw = new QueryWrapper<>();
        qw.eq("username", username);
        UsersEntity user = this.getOne(qw);

        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }

        // 生成Token
        String role = user.getRole() != null ? user.getRole() : "管理员";
        String token = tokenService.generateToken(user.getId(), username, "users", role);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userid", user.getId());
        result.put("username", user.getUsername());
        result.put("role", role);
        return result;
    }
}
