package com.yyyysq.mall_gateway.controller.config.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//先用拦截器来实现登录权限框架，等后期再使用spring security来使项目更加简洁
@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //这个方法才是调用目标方法前会执行的拦截方法。
        String requestServletPath = request.getServletPath(); //获取请求的url地址
        if(requestServletPath.startsWith("/admin")&&request.getSession().getAttribute("loginUser") == null){ //看是否登录getSession()可以检验是否是第一次登录
            request.getSession().setAttribute("errorMsg", "没有权限，请登录");
            response.sendRedirect("/admin/login");
            return false;
        }else{
            request.getSession().getAttribute("errorMsg");  //如果已经登录，就需要把这个存在seesion的错误信息去掉，不然登录后会一直保存着
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //这个方法是在执行完目标方法后倒序遍历每个拦截器时调用这个方法。
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //这个方法是在渲染页面结束后倒序遍历拦截器时调用每个拦截器的这个方法
    }
}
