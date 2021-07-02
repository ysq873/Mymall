package com.yyyysq.mall_goods_service.dao;


import com.yyyysq.mall_goods.entity.MallShoppingCart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallShoppingCartMapper {

    int deleteByPrimaryKey(Long cartItemId);

    int insert(MallShoppingCart record);

    int insertSelective(MallShoppingCart record);

    MallShoppingCart selectByPrimaryKey(Long cartItemId);

    MallShoppingCart selectByUserIdAndGoodsId(@Param("MallUserId") Long MallUserId, @Param("goodsId") Long goodsId);

    List<MallShoppingCart> selectByUserId(@Param("MallUserId") Long MallUserId, @Param("number") int number);

    int selectCountByUserId(Long MallUserId);

    int updateByPrimaryKeySelective(MallShoppingCart record);

    int updateByPrimaryKey(MallShoppingCart record);

    int deleteBatch(List<Long> ids);
}
