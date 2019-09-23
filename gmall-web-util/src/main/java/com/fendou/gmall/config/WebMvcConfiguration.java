package com.fendou.gmall.config;

import com.fendou.gmall.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// 注意一定不要加@Configuration注解
public class WebMvcConfiguration implements WebMvcConfigurer{
    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/index/css/**","/index/img/**","/index/js/**","/index/json/**"
        ,"/list/css/**","/list/font/**","/list/image/**","/list/img/**","/list/js/**","/bootstrap/**","/css/**","/image/**","/img/**","/js/**","/scss/**"
        ,"/JD_img/**","/JD_js/**","/JD_sass/**");

    }
}