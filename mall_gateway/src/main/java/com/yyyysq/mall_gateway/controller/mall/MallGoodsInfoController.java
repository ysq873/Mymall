package com.yyyysq.mall_gateway.controller.mall;


import com.alibaba.dubbo.config.annotation.Reference;
import com.yyyysq.mall_config.common.Constants;
import com.yyyysq.mall_config.utils.MyBeanUtil;
import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_config.utils.PageResult;

import com.yyyysq.mall_goods.VO.MallGoodsDetailVO;
import com.yyyysq.mall_goods.VO.MallSearchGoodsCategoryVO;
import com.yyyysq.mall_goods.VO.MallSearchGoodsVO;
import com.yyyysq.mall_goods.entity.MallGoods;
import com.yyyysq.mall_goods.serviceApi.MallCategoryService;
import com.yyyysq.mall_goods.serviceApi.MallGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;

import java.util.Map;

@Controller
public class MallGoodsInfoController {

    @Reference
    MallGoodsService mallGoodsService;

    @Reference
    MallCategoryService mallCategoryService;

    @GetMapping({"/search", "/search.html"})
    public String searchPage(@RequestParam Map<String,Object> params,
                             HttpServletRequest request) {
        Integer page;
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
            page = (Integer)params.get("page");
        }else{
            String oldPage = (String)params.get("page");
            page = Integer.valueOf(oldPage);
        }
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        //封装分类数据
        if (params.containsKey("goodsCategoryId") && !StringUtils.isEmpty(params.get("goodsCategoryId") + "")) {
            Long categoryId = Long.valueOf(params.get("goodsCategoryId") + "");
            MallSearchGoodsCategoryVO searchPageCategoryVO = mallCategoryService.getCategoriesForSearch(categoryId);
            if (searchPageCategoryVO != null) {
                request.setAttribute("goodsCategoryId", categoryId);
                request.setAttribute("searchPageCategoryVO", searchPageCategoryVO);
            }
        }
        //封装参数供前端回显
        if (params.containsKey("orderBy") && !StringUtils.isEmpty(params.get("orderBy") + "")) {
            request.setAttribute("orderBy", params.get("orderBy") + "");
        }
        //对keyword做过滤 去掉空格
//        if (keyword != null && !StringUtils.isEmpty(keyword.trim())) {
//            keyword = keyword+"";
//        }
        String keyword = "";
        if(params.containsKey("keyword") && !StringUtils.isEmpty((params.get("keyword")+"").trim())){
            keyword = params.get("keyword") + "";
        }
        request.setAttribute("keyword", keyword);
        params.put("keyword", keyword);
        //搜索上架状态下的商品
        params.put("goodsSellStatus", Constants.SELL_STATUS_UP);
        PageResult result = mallGoodsService.searchMallGoods(params,page,Constants.ORDER_SEARCH_PAGE_LIMIT);
        System.out.println("拿到了pageResult数据");
        request.setAttribute("pageResult", result);
        return "mall/search";
    }

    @GetMapping("/goods/detail/{goodsId}")
    public String detailPage(@PathVariable("goodsId") Long goodsId, HttpServletRequest request) {
        if (goodsId < 1) {
            return "error/error_5xx";
        }
        MallGoods goods = mallGoodsService.getMallGoodsById(goodsId);
        if (goods == null) {
            return "error/error_404";
        }
        MallGoodsDetailVO goodsDetailVO = new MallGoodsDetailVO();
        MyBeanUtil.copyProperties(goods, goodsDetailVO);
        request.setAttribute("goodsDetail", goodsDetailVO);
        System.out.println(goodsDetailVO.getGoodsDetailContent());
        return "mall/detail";
    }
}
