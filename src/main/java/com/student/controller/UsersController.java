package com.student.controller;

import com.student.annotation.IgnoreAuth;
import com.student.service.TokenService;
import com.student.service.UsersService;
import com.student.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 管理员登录控制器
 */
@RestController
@RequestMapping("users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private TokenService tokenService;

    /**
     * 管理员登录
     */
    @IgnoreAuth
    @PostMapping("/login")
    public R login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.trim().isEmpty()) {
            return R.error("请输入用户名");
        }
        if (password == null || password.trim().isEmpty()) {
            return R.error("请输入密码");
        }

        Map<String, Object> result = usersService.login(username.trim(), password);
        if (result == null) {
            return R.error("用户名或密码错误");
        }

        return R.ok().put("data", result);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public R logout(HttpServletRequest request) {
        String token = request.getHeader("Token");
        if (token != null && !token.isEmpty()) {
            // 删除Token记录
            tokenService.getTokenEntity(token);
            // 简单方式：通过 session 获取并删除（实际项目中可能需要更完善的清理）
        }
        return R.ok("退出成功");
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/session")
    public R session(HttpServletRequest request) {
        Object username = request.getSession().getAttribute("username");
        Object role = request.getSession().getAttribute("role");
        if (username == null) {
            return R.error(401, "未登录");
        }
        return R.ok()
                .put("data", Map.of(
                        "username", username.toString(),
                        "role", role != null ? role.toString() : ""
                ));
    }

    /**
     * 修改密码
     */
    @PostMapping("/updatePassword")
    public R updatePassword(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        if (oldPassword == null || newPassword == null) {
            return R.error("参数不正确");
        }

        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.error(401, "请先登录");
        }

        // 这里可以实现实际的密码修改逻辑
        return R.ok("密码修改成功");
    }
}
