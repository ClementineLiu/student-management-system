package com.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.dao.TokenDao;
import com.student.entity.TokenEntity;
import com.student.service.TokenService;
import com.student.utils.CommonUtil;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * Token服务实现
 */
@Service("tokenService")
public class TokenServiceImpl extends ServiceImpl<TokenDao, TokenEntity> implements TokenService {

    @Override
    public String generateToken(Long userid, String username, String tableName, String role) {
        // 查询是否已有该用户的Token
        QueryWrapper<TokenEntity> qw = new QueryWrapper<>();
        qw.eq("userid", userid).eq("role", role);
        TokenEntity tokenEntity = this.getOne(qw);

        // 生成32位随机Token
        String token = CommonUtil.getRandomString(32);

        // 过期时间 = 当前时间 + 1小时
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 1);

        if (tokenEntity != null) {
            // 更新已有Token
            tokenEntity.setToken(token);
            tokenEntity.setExpiratedtime(cal.getTime());
            this.updateById(tokenEntity);
        } else {
            // 新增Token
            this.save(new TokenEntity(userid, username, tableName, role, token, cal.getTime()));
        }

        return token;
    }

    @Override
    public TokenEntity getTokenEntity(String token) {
        QueryWrapper<TokenEntity> qw = new QueryWrapper<>();
        qw.eq("token", token);
        TokenEntity tokenEntity = this.getOne(qw);

        if (tokenEntity == null || tokenEntity.getExpiratedtime() == null) {
            return null;
        }

        // 检查是否过期
        if (tokenEntity.getExpiratedtime().getTime() < new Date().getTime()) {
            return null;
        }

        return tokenEntity;
    }
}
