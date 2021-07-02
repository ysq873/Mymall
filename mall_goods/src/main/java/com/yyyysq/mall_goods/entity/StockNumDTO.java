package com.yyyysq.mall_goods.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class StockNumDTO implements Serializable {
    private Long goodsId;

    private Integer goodsCount;
}
