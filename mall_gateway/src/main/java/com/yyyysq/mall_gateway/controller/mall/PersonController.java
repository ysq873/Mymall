package com.yyyysq.mall_gateway.controller.mall;


import com.alibaba.dubbo.config.annotation.Reference;
import com.yyyysq.mall_config.common.Constants;
import com.yyyysq.mall_config.common.ServiceResultEnum;
import com.yyyysq.mall_config.utils.MD5Util;
import com.yyyysq.mall_config.utils.MyBeanUtil;
import com.yyyysq.mall_config.utils.Result;
import com.yyyysq.mall_config.utils.ResultGenerator;
import com.yyyysq.mall_gateway.controller.vo.MallUserVO;
import com.yyyysq.mall_goods.entity.MallShoppingCart;
import com.yyyysq.mall_goods.serviceApi.MallShoppingCartService;
import com.yyyysq.mall_user.dto.MallUserDTO;
import com.yyyysq.mall_user.entity.MallUser;
import com.yyyysq.mall_user.serviceApi.MallUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class PersonController {

    @Reference
    MallUserService mallUserService;

    @Resource
    RedisTemplate redisTemplate;

    /**
     * 登陆页面跳转
     * @return
     */
    @GetMapping({"/login", "login.html"})
    public String loginPage() {
        return "mall/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestParam("loginName") String loginName,
                        @RequestParam("verifyCode") String verifyCode,
                        @RequestParam("password") String password,
                        HttpServletResponse response,
                        HttpSession session) {
        if (StringUtils.isEmpty(loginName)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if (StringUtils.isEmpty(password)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
        }
        if (StringUtils.isEmpty(verifyCode)) {

            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.toLowerCase().equals(kaptchaCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
        }
        //todo 清verifyCode
        String loginResult = mallUserService.login(loginName, MD5Util.MD5Encode(password, "UTF-8"));
        //登录成功
        if (loginResult!=null) {
            Cookie cookie = new Cookie("token", loginResult);
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResultGenerator.genSuccessResult((Object)loginResult);
        }
        //登录失败
        return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_ERROR.getResult());
    }

    @GetMapping("/getUserToIndex")
    @ResponseBody
    public Result getUser(HttpServletRequest request){
        String token = request.getHeader("token");
        if(token != null && !StringUtils.isEmpty(token)){
            MallUserDTO mallUserDTO = (MallUserDTO)redisTemplate.opsForValue().get(token);
            MallUserVO mallUserVO = new MallUserVO();
            MyBeanUtil.copyProperties(mallUserDTO, mallUserVO);
            HttpSession session = request.getSession();
            session.setAttribute(Constants.MALL_USER_SESSION_KEY,mallUserVO);
            return ResultGenerator.genSuccessResult(mallUserVO);
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_ERROR.getResult());
    }



    /**
     * 注册页面跳转
     * @return
     */
    @GetMapping({"/register", "register.html"})
    public String registerPage() {
        return "mall/register";
    }

    @PostMapping("/register")
    @ResponseBody
    public Result register(@RequestParam("loginName") String loginName,
                           @RequestParam("verifyCode") String verifyCode,
                           @RequestParam("password") String password,
                           HttpSession httpSession) {
        if (StringUtils.isEmpty(loginName)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if (StringUtils.isEmpty(password)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
        }
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
        }
        String kaptchaCode = httpSession.getAttribute(Constants.MALL_VERIFY_CODE_KEY) + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
        }
        String registerResult = mallUserService.register(loginName, password);
        //注册成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //注册失败
        return ResultGenerator.genFailResult(registerResult);
    }

    @GetMapping("/logout")
    @ResponseBody
    public Result logout(HttpServletRequest request,HttpServletResponse response) {
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute(Constants.MALL_USER_SESSION_KEY));
        session.removeAttribute(Constants.MALL_USER_SESSION_KEY);
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
                if("token".equals(cookie.getName())){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    response.setDateHeader("Expires", 0);
                    response.setHeader("Cache-Control", "no-cache");
                    response.setHeader("Pragma", "no-cache");
                }
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/personal/updateAddress")
    @ResponseBody
    public Result updateAddress(@RequestBody MallUser mallUser, HttpSession httpSession) {
        MallUserVO mallUserVO = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        MallUserDTO oldMallUserDTO = new MallUserDTO();
        MyBeanUtil.copyProperties(mallUserVO, oldMallUserDTO);
        MallUserDTO mallUserDTO = mallUserService.updateAddress(mallUser,oldMallUserDTO);
        MyBeanUtil.copyProperties(mallUserDTO,mallUserVO);
        httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY,mallUserVO);
        if (mallUserDTO == null) {
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        } else {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        }
    }
    @PostMapping("/personal/updateInfo")
    @ResponseBody
    public Result updateInfo(@RequestBody MallUser mallUser, HttpSession httpSession) {
        MallUserVO mallUserVO = (MallUserVO)httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        MallUserDTO mallUserDTO = new MallUserDTO();
        MyBeanUtil.copyProperties(mallUserVO, mallUserDTO);
        MallUserDTO mallUserTemp = mallUserService.updateUserInfo(mallUser,mallUserDTO);
        MyBeanUtil.copyProperties(mallUserTemp,mallUserVO);
        if (mallUserTemp == null) {
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        } else {
            httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, mallUserVO);
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        }
    }
    @GetMapping("/personal")
    public String personalPage(HttpServletRequest request,
                               HttpSession httpSession) {
        request.setAttribute("path", "personal");
        return "mall/personal";
    }
}
