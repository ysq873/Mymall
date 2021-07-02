package com.yyyysq.mall_goods_service.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.yyyysq.mall_config.common.ServiceResultEnum;
import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_config.utils.PageResult;
import com.yyyysq.mall_goods.VO.MallIndexCarouselVO;
import com.yyyysq.mall_goods.dto.MallIndexCarouselDTO;
import com.yyyysq.mall_goods.entity.Carousel;
import com.yyyysq.mall_goods.serviceApi.MallCarouselService;
import com.yyyysq.mall_goods_service.dao.MallCarouselMapper;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MallCarouselServiceImpl implements MallCarouselService {

    @Resource
    MallCarouselMapper mallCarouselMapper;

    @Override
    public PageResult getCarouselPage(PageQueryUtil pageQueryUtil) {
        List<Carousel> carousels = mallCarouselMapper.getCarousels(pageQueryUtil);
        int total = mallCarouselMapper.getTotalCarousel(pageQueryUtil);
        PageResult pageResult = new PageResult(carousels,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveCarousel(Carousel carousel) {
        if (mallCarouselMapper.insertSelective(carousel) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除轮播图数据
        return mallCarouselMapper.deleteBatch(ids) > 0;
    }

    @Override
    public List<MallIndexCarouselVO> getCarouselForIndex() {
        return mallCarouselMapper.getAllCarousels();
    }
}
