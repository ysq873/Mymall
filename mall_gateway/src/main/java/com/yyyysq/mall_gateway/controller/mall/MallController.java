package com.yyyysq.mall_gateway.controller.mall;


import com.alibaba.dubbo.config.annotation.Reference;
import com.yyyysq.mall_config.common.Constants;
import com.yyyysq.mall_config.common.IndexConfigTypeEnum;
import com.yyyysq.mall_goods.VO.MallIndexCarouselVO;
import com.yyyysq.mall_goods.VO.MallIndexCategoryVO;
import com.yyyysq.mall_goods.VO.MallIndexConfigGoodsVO;
import com.yyyysq.mall_goods.serviceApi.MallCarouselService;
import com.yyyysq.mall_goods.serviceApi.MallCategoryService;
import com.yyyysq.mall_goods.serviceApi.MallIndexConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MallController {

    @Reference
    MallCarouselService mallCarouselService;

    @Reference
    MallCategoryService mallCategoryService;

    @Reference
    MallIndexConfigService mallIndexConfigService;

    @RequestMapping({"/","/index","/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<MallIndexCarouselVO> carousels = mallCarouselService.getCarouselForIndex();
        List<MallIndexCategoryVO> categories = mallCategoryService.getCategoriesForIndex();
        List<MallIndexConfigGoodsVO> hotGoods = mallIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<MallIndexConfigGoodsVO> newGoods = mallIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<MallIndexConfigGoodsVO> recommendGoods = mallIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        HttpSession session = request.getSession();
        session.setAttribute("hotGoodses", hotGoods);
        session.setAttribute("newGoodses", newGoods);
        session.setAttribute("recommendGoodses", recommendGoods);
        session.setAttribute("carousels", carousels);
        session.setAttribute("categories", categories);
        return "mall/index";
    }


}
