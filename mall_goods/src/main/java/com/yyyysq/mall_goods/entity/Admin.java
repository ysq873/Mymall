package com.yyyysq.mall_goods.entity;

import lombok.Data;

import java.io.Serializable;

//商场管理员
@Data
public class Admin implements Serializable {

    //管理员id
    private Integer adminUserId;

    //管理员登录账号
    private String loginUserName;

    //登陆密码
    private String loginPassword;

    //管理员昵称
    private String nickName;

    //是否锁定，锁定就无法登录了
    private Byte locked;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", adminUserId=").append(adminUserId);
        sb.append(", loginUserName=").append(loginUserName);
        sb.append(", loginPassword=").append(loginPassword);
        sb.append(", nickName=").append(nickName);
        sb.append(", locked=").append(locked);
        sb.append("]");
        return sb.toString();
    }
}