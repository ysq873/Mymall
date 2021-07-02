package com.yyyysq.mall_goods.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class MallGoods implements Serializable {
    //商品id
    private Long goodsId;

    //商品名称
    private String goodsName;

    //商品简介
    private String goodsIntro;

    //商品分类id
    private Long goodsCategoryId;

    //商品主图
    private String goodsCoverImg;

    //商品轮播图
    private String goodsCarousel;

    //原始价格
    private Integer originalPrice;

    //实际售卖价格
    private Integer sellingPrice;

    //库存
    private Integer stockNum;

    //标签
    private String tag;

    //商品销售状态
    private Byte goodsSellStatus;

    //创建商品者
    private Integer createUser;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    //更改者
    private Integer updateUser;

    //更改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    //商品描述
    private String goodsDetailContent;
}
