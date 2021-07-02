package com.yyyysq.mall_gateway.controller.config.interceptor;


import com.yyyysq.mall_config.common.Constants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class MallLoginInterceptor implements HandlerInterceptor {

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Authorization,token");
        Cookie[] cookies = request.getCookies();
//        request.getHeader("token");
        String token = null;
        for(Cookie cookie : cookies){
            if("token".equals(cookie.getName())){
                token = cookie.getValue();
            }
        }
        System.out.println(token+"过滤器token");
        //判断token是否为null
        token = token == null ? "" : token;
        //判断token是否过时
        if(redisTemplate.getExpire(token) <= 0){
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }else{
            //重置token时间
            redisTemplate.expire(token, 30L, TimeUnit.MINUTES);
            System.out.println("修改了时间");
            return true;
        }
    }
}
