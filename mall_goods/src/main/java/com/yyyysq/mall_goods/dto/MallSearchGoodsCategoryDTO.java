package com.yyyysq.mall_goods.dto;


import com.yyyysq.mall_goods.entity.GoodsCategory;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Data
@ToString
public class MallSearchGoodsCategoryDTO implements Serializable {
    private String firstLevelCategoryName;

    private List<GoodsCategory> secondLevelCategoryList;

    private String secondLevelCategoryName;

    private List<GoodsCategory> thirdLevelCategoryList;

    private String currentCategoryName;
}
