package com.yyyysq.mall_user.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MallUserDTO implements Serializable {
    private Long userId;

    private String nickName;

    private String loginName;

    private String introduceSign;

    private String address;

    private int shopCartItemCount;
}
