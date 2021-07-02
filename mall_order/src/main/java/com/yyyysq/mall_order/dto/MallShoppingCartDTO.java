package com.yyyysq.mall_order.dto;

import lombok.Data;

@Data
public class MallShoppingCartDTO {
    private Long cartItemId;

    private Long goodsId;

    private Integer goodsCount;

    private String goodsName;

    private String goodsCoverImg;

    private Integer sellingPrice;
}
