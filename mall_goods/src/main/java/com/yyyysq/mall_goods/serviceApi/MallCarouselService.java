package com.yyyysq.mall_goods.serviceApi;


import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_config.utils.PageResult;
import com.yyyysq.mall_goods.VO.MallIndexCarouselVO;
import com.yyyysq.mall_goods.dto.MallIndexCarouselDTO;
import com.yyyysq.mall_goods.entity.Carousel;

import java.util.List;

public interface MallCarouselService {

    public PageResult getCarouselPage(PageQueryUtil pageQueryUtil);

//    public Integer getCarouseTotal(PageQueryUtil pageQueryUtil);

    public String saveCarousel(Carousel carousel);

    public boolean deleteBatch(Integer ids[]);

    public List<MallIndexCarouselVO> getCarouselForIndex();

}
