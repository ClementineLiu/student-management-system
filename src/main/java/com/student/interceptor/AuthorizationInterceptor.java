package com.student.interceptor;

import com.student.annotation.IgnoreAuth;
import com.student.entity.TokenEntity;
import com.student.service.TokenService;
import com.student.utils.R;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Token 验证拦截器
 * 校验请求Header中的Token是否有效
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 非Controller方法直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 检查是否有 @IgnoreAuth 注解，有则放行
        if (method.isAnnotationPresent(IgnoreAuth.class)) {
            return true;
        }

        // 从 Header 中获取 Token
        String token = request.getHeader("Token");
        if (token == null || token.trim().isEmpty()) {
            returnJson(response, R.error(401, "请先登录"));
            return false;
        }

        // 验证 Token
        TokenEntity tokenEntity = tokenService.getTokenEntity(token);
        if (tokenEntity == null) {
            returnJson(response, R.error(401, "Token无效或已过期，请重新登录"));
            return false;
        }

        // 将用户信息存入 Session
        request.getSession().setAttribute("userId", tokenEntity.getUserid());
        request.getSession().setAttribute("username", tokenEntity.getUsername());
        request.getSession().setAttribute("role", tokenEntity.getRole());
        request.getSession().setAttribute("tableName", tokenEntity.getTablename());

        return true;
    }

    private void returnJson(HttpServletResponse response, R r) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JSON.toJSONString(r));
        writer.flush();
        writer.close();
    }
}
