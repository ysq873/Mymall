package com.yyyysq.mall_order.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class MallOrderItem implements Serializable {
    private Long orderItemId;

    private Long orderId;

    private Long goodsId;

    private String goodsName;

    private String goodsCoverImg;

    private Integer sellingPrice;

    private Integer goodsCount;

    private Date createTime;
}
