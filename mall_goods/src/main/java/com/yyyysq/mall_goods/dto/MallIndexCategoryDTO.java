package com.yyyysq.mall_goods.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class MallIndexCategoryDTO implements Serializable {
    private Long categoryId;

    private Byte categoryLevel;

    private String categoryName;

    private List<SecondLevelCategoryDTO> secondLevelCategoryVOS;
}
