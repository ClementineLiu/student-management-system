package com.student.config;

import com.student.interceptor.AuthorizationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置 + 静态资源配置
 */
@Configuration
public class IntercaptorConfig implements WebMvcConfigurer {

    @Bean
    public AuthorizationInterceptor getAuthorizationInterceptor() {
        return new AuthorizationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthorizationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/js/**", "/css/**", "/fonts/**", "/img/**", "/favicon.ico", "/index.html", "/", "/error");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 保留 admin 目录映射（构建产物路径）
        registry.addResourceHandler("/admin/**")
                .addResourceLocations("classpath:/admin/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 将根路径转发到 index.html（Vue SPA 入口）
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
