package com.yyyysq.mall_goods.serviceApi;


import com.yyyysq.mall_goods.VO.MallShoppingCartVO;
import com.yyyysq.mall_goods.dto.MallShoppingCartDTO;
import com.yyyysq.mall_goods.entity.MallShoppingCart;

import java.util.List;

public interface MallShoppingCartService {



    /**
     * 添加商品到购物车
     * @param mallShoppingCart
     * @return
     */
    public String saveMallCartItem(MallShoppingCart mallShoppingCart);

    /**
     * 更新购物车信息
     * @param mallShoppingCart
     * @return
     */
    public String updateMallCart(MallShoppingCart mallShoppingCart);

    /**
     * 获取购物车信息
     * @param userId
     * @return
     */
    public List<MallShoppingCartVO> getMyShoppingCartItems(Long userId);

    /**
     * 修改购物车中的属性
     *
     * @param mallShoppingCart
     * @return
     */
    String updateMallCartItem(MallShoppingCart mallShoppingCart);

    /**
     * 删除购物车中的商品
     *
     * @param mallShoppingCartId
     * @return
     */
    Boolean deleteById(Long mallShoppingCartId);

    Boolean deleteByIds(List<Long> mallShoppingCartIds);
}
