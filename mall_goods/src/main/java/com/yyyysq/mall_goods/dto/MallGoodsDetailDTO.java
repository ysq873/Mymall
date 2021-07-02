package com.yyyysq.mall_goods.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MallGoodsDetailDTO implements Serializable {
    private Long goodsId;

    private String goodsName;

    private String goodsIntro;

    private String goodsCoverImg;

    private String[] goodsCarouselList;

    private Integer sellingPrice;

    private Integer originalPrice;

    private String goodsDetailContent;
}
