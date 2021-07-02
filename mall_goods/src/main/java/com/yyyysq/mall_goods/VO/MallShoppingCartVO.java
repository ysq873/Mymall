package com.yyyysq.mall_goods.VO;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
public class MallShoppingCartVO implements Serializable {
    //购物车id
    private Long cartItemId;

    //商品id
    private Long goodsId;

    //该商品数量
    private Integer goodsCount;

    private String goodsName;

    private String goodsCoverImg;
    //商品价格
    private Integer sellingPrice;

}
