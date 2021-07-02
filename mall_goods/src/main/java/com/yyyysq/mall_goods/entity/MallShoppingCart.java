package com.yyyysq.mall_goods.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class MallShoppingCart implements Serializable {
    private Long cartItemId;

    private Long userId;

    private Long goodsId;

    private Integer goodsCount;

    private Byte isDeleted;

    private Date createTime;

    private Date updateTime;
}
