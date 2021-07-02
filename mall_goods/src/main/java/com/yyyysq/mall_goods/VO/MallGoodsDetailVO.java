package com.yyyysq.mall_goods.VO;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MallGoodsDetailVO implements Serializable {
    private Long goodsId;

    private String goodsName;

    private String goodsIntro;

    private String goodsCoverImg;

    private String[] goodsCarouselList;

    private Integer sellingPrice;

    private Integer originalPrice;

    private String goodsDetailContent;
}
