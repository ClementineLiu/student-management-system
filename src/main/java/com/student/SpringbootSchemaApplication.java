package com.student;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 学生管理系统 - 启动类
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.student.dao"})
public class SpringbootSchemaApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSchemaApplication.class, args);
        System.out.println("========================================");
        System.out.println("  学生管理系统启动成功！");
        System.out.println("  访问地址：http://localhost:8080/student");
        System.out.println("========================================");
    }
}
