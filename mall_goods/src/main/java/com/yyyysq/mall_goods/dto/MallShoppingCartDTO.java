package com.yyyysq.mall_goods.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
public class MallShoppingCartDTO implements Serializable {
    private Long cartItemId;

    private Long goodsId;

    private Integer goodsCount;

    private String goodsName;

    private String goodsCoverImg;

    private Integer sellingPrice;

}
