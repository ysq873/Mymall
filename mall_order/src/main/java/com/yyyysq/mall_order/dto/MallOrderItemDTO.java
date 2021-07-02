package com.yyyysq.mall_order.dto;

import lombok.Data;

import java.io.Serializable;

    @Data
    public class MallOrderItemDTO implements Serializable {
        private Long goodsId;

        private Integer goodsCount;

        private String goodsName;

        private String goodsCoverImg;

        private Integer sellingPrice;
    }

