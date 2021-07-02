package com.yyyysq.mall_gateway.controller.vo;

import com.yyyysq.mall_goods.entity.GoodsCategory;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class MallSearchGoodsCategoryVO implements Serializable {
    private String firstLevelCategoryName;

    private List<GoodsCategory> secondLevelCategoryList;

    private String secondLevelCategoryName;

    private List<GoodsCategory> thirdLevelCategoryList;

    private String currentCategoryName;
}
