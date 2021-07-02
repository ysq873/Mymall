package com.yyyysq.mall_goods.serviceApi;


import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_config.utils.PageResult;
import com.yyyysq.mall_goods.VO.MallIndexConfigGoodsVO;
import com.yyyysq.mall_goods.dto.MallIndexConfigGoodsDTO;
import com.yyyysq.mall_goods.entity.IndexConfig;

import java.util.List;

public interface MallIndexConfigService {

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<MallIndexConfigGoodsVO> getConfigGoodsForIndex(int configType, int number);

    Boolean deleteBatch(Long[] ids);
}
