package com.yyyysq.mall_gateway.controller.config;

import com.yyyysq.mall_config.common.Constants;
import com.yyyysq.mall_gateway.controller.config.interceptor.AdminInterceptor;
import com.yyyysq.mall_gateway.controller.config.interceptor.MallLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MallWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    MallLoginInterceptor mallLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mallLoginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/","/index","/index.html")
                .excludePathPatterns("/admin/dist/**")
                .excludePathPatterns("/admin/plugins/**")
                .excludePathPatterns("/mall/**")
                .excludePathPatterns("/common/**")
                .excludePathPatterns("/goods-img/**")
                .excludePathPatterns("/register")
                .excludePathPatterns("register.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //当请求资源路径为upload/**时,会自动到我们配置的磁盘的位置去查找
        registry.addResourceHandler("/upload/**").addResourceLocations("file:D:\\JSP\\upload\\");
        registry.addResourceHandler("/goods-img/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
    }
}
