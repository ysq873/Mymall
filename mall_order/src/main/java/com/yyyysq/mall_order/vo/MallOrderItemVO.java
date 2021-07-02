package com.yyyysq.mall_order.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MallOrderItemVO implements Serializable {
    private Long goodsId;

    private Integer goodsCount;

    private String goodsName;

    private String goodsCoverImg;

    private Integer sellingPrice;
}
