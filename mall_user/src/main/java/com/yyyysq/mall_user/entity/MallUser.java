package com.yyyysq.mall_user.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

//商城用户
@Data
@ToString
public class MallUser implements Serializable {

    //用户id
    private Long userId;

    //用户昵称
    private String nickName;

    //登陆账号
    private String loginName;

    //登陆密码,这里已经经历过前端一次MD5加密了
    private String passwordMd5;

    //个性签名
    private String introduceSign;

    //地址
    private String address;

    //是否已经被注销
    private Integer isDeleted;

    //是否已经被锁定
    private Integer lockedFlag;

    //创建时间
    private Date createTime;

}
