package com.yyyysq.mall_gateway.controller.common;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Controller
@RequestMapping("/common")
public class KaptchaController{

    @Autowired
    private DefaultKaptcha KaptchaProducer;

    @GetMapping("/kaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生成验证码字符串保存在session中
            String verifyCode = KaptchaProducer.createText();
            System.out.println(verifyCode);
            httpServletRequest.getSession().setAttribute("verifyCode", verifyCode);
            BufferedImage challenge = KaptchaProducer.createImage(verifyCode);
            ImageIO.write(challenge, "jpg", imgOutputStream);
        }catch(IllegalArgumentException e){
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaOutputStream = imgOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store"); //设置后资源禁止被缓存
        httpServletResponse.setHeader("Pragma", "no-cache");  //不会被缓存
        httpServletResponse.setDateHeader("Expires", 0); // 有两种，一种是相对文件请求时间（A），一种是相对文件创建时间（M）。
        httpServletResponse.setContentType("image/jpeg"); //设置类型
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
    @GetMapping("/kaptcha.html")
    public String kaptcha(){
            return "kaptcha";
    }

    @GetMapping("/verify")
    @ResponseBody
    public String verify(@RequestParam("code")String code, HttpSession session){
        if(StringUtils.isEmpty(code)){
            return "验证码不能为空";
        }
        String kaptchaCode = session.getAttribute("verifyCode")+"";  //从session获取存在服务器端的验证码
        if(StringUtils.isEmpty(kaptchaCode) || !code.equals(kaptchaCode)){
            System.out.println(kaptchaCode);
            return "验证码错误";
        }
        return "验证成功";
    }
    @GetMapping("/verify.html")
    public String verifyHtml(){
            return "verify";
    }

}
