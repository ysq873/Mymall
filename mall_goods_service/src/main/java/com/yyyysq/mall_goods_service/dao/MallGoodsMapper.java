package com.yyyysq.mall_goods_service.dao;


import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate;
import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_goods.entity.MallGoods;
import com.yyyysq.mall_goods.entity.StockNumDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallGoodsMapper {

    Integer insertSelective(MallGoods mallGoods);

    MallGoods getGoodById(Long goodsId);

    Integer updateByPrimaryKeySelective(MallGoods goods);

    Integer getTotalMallGoods(PageQueryUtil pageQueryUtil);

    List<MallGoods> findMallGoodsList(PageQueryUtil pageQueryUtil);

    Integer batchUpdateSellStatus(@Param("ids") Long[] ids, @Param("sellStatus") int sellStatus);

    List<MallGoods> selectByPrimaryKeys(List<Long> ids);

    List<MallGoods> findMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalMallGoodsBySearch(PageQueryUtil pageUtil);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

}
