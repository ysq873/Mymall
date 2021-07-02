package com.yyyysq.mall_goods_service.dao;


import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_goods.VO.MallIndexCarouselVO;
import com.yyyysq.mall_goods.dto.MallIndexCarouselDTO;
import com.yyyysq.mall_goods.entity.Carousel;

import java.util.List;

public interface MallCarouselMapper {

    public List<Carousel> getCarousels(PageQueryUtil pageQueryUtil);

    public Integer getTotalCarousel(PageQueryUtil pageQueryUtil);

    public Integer insertSelective(Carousel carousel);

    public Integer deleteBatch(Integer[] ids);

    public List<MallIndexCarouselVO> getAllCarousels();

}

