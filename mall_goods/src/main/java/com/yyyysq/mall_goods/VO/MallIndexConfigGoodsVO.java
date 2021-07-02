package com.yyyysq.mall_goods.VO;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 *商品详情页VO
 */
@Data
@ToString
public class MallIndexConfigGoodsVO implements Serializable {
    private Long goodsId;

    private String goodsName;

    private String goodsIntro;

    private String goodsCoverImg;

    private Integer sellingPrice;

    private String tag;
}
