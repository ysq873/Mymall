package com.yyyysq.mall_goods.VO;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class MallIndexCategoryVO implements Serializable {
    private Long categoryId;

    private Byte categoryLevel;

    private String categoryName;

    private List<SecondLevelCategoryVO> secondLevelCategoryVOS;
}
