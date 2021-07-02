package com.yyyysq.mall_gateway.controller.common;


import com.yyyysq.mall_config.common.Constants;
import com.yyyysq.mall_config.utils.MallUtil;
import com.yyyysq.mall_config.utils.Result;
import com.yyyysq.mall_config.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

    @Controller
    public class UploadController {

    @Autowired
    StandardServletMultipartResolver standardServletMultipartResolver;

    @RequestMapping(value = "/upload/file", method = RequestMethod.POST)
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) throws URISyntaxException {
	//
        if (file.isEmpty()) {
            return ResultGenerator.genFailResult("文件异常");
        }
        String fileName = file.getOriginalFilename(); //获取文件名称
        System.out.println(fileName);
        String suffixName = fileName.substring(fileName.lastIndexOf(".")); //获取文件类型
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC); //如果没有这个目录就创建这个目录
        File newFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);  //如果没有这个文件就创建这个文件
        try {
            if (!fileDirectory.exists()) { //测试文件是否存在
                if (!fileDirectory.mkdir()) {     //如果不存在就创建一个，如果创建失败则抛出异常
                    throw new IOException("文件创建失败，路径为:" + fileDirectory);
                }
            }
            file.transferTo(newFile);
            Result result = ResultGenerator.genSuccessResult();
            result.setData("/upload/" + newFileName);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("上传失败");
        }
    }

    @RequestMapping("upload/files")
    @ResponseBody
    public Result uploadFiles(HttpServletRequest httpServletRequest) throws URISyntaxException {
        List<MultipartFile> files = new ArrayList<>();
        //先判断是否是文件上传请求
        if (standardServletMultipartResolver.isMultipart(httpServletRequest)) {
            //将变量类型改为文件上传请求
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) httpServletRequest;
            Iterator<String> fileNames = multipartRequest.getFileNames();
            int size = 0;
            while (fileNames.hasNext()) {
                if (size > 5) {
                    return ResultGenerator.genFailResult("图片数量不能超过5张");
                }
                files.add(multipartRequest.getFile(fileNames.next()));  //将所有文件放到文件列表中
                size++;
            }
            if (CollectionUtils.isEmpty(files)) {
                return ResultGenerator.genFailResult("参数异常");
            }
            if (files != null && files.size() > 5) {
                return ResultGenerator.genFailResult("最多上传5张图片");
            }
        }
            List<String> fileStoreName = new ArrayList<>();
            for (MultipartFile file : files) {
                String filename = file.getOriginalFilename(); //获取文件原始名称
                String suffixName = filename.substring(filename.lastIndexOf("."));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                Random random = new Random();
                StringBuilder newFileName = new StringBuilder();
                //随机名称，由当前时间和随机生成数再加上文件后缀
                newFileName.append(sdf.format(new Date())).append(random.nextInt(100)).append(suffixName);
                //创建文件类型对象指向这个目录
                File directory = new File(Constants.FILE_UPLOAD_DIC); //目录
                //创建文件
                File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
                try{
                if(!directory.exists()){
                    if(!directory.mkdir()){
                        throw new IOException("文件创建失败，目录为" + directory);
                    }
                }
                //将这个文件复制给目标文件
                file.transferTo(destFile);
                //为了让前端能接受我们存储图片的真实地址，需要获取本机地址并将文件的存储地位也加在后面
                fileStoreName.add(MallUtil.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/" + newFileName);
                for(String fileName : fileStoreName){
                    System.out.println(fileName);
                }
                }
                catch(IOException e){
                    e.printStackTrace();
                    return ResultGenerator.genFailResult("文件上传失败");
                }
            }
            Result result = ResultGenerator.genSuccessResult();
            result.setData(fileStoreName);
            return result;
        }

        @GetMapping("/uploadTest")
        public String upload1(){
           return "uploadTest";
        }

    }

