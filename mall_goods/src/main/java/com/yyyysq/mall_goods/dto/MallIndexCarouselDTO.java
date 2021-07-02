package com.yyyysq.mall_goods.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MallIndexCarouselDTO implements Serializable {

    //由于前端只需要用到这三个字段，不需要将整个Carousel实体类都找出来

    private Integer carouselId;

    private String carouselUrl;

    private String redirectUrl;

}
