package com.yyyysq.mall_goods_service.service;



import com.alibaba.dubbo.config.annotation.Service;
import com.yyyysq.mall_config.common.Constants;
import com.yyyysq.mall_config.common.ServiceResultEnum;
import com.yyyysq.mall_config.utils.MyBeanUtil;
import com.yyyysq.mall_goods.VO.MallShoppingCartVO;
import com.yyyysq.mall_goods.entity.MallGoods;
import com.yyyysq.mall_goods.entity.MallShoppingCart;
import com.yyyysq.mall_goods.serviceApi.MallShoppingCartService;
import com.yyyysq.mall_goods_service.dao.MallGoodsMapper;
import com.yyyysq.mall_goods_service.dao.MallShoppingCartMapper;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MallShoppingCartServiceImpl implements MallShoppingCartService {

    @Resource
    MallShoppingCartMapper mallShoppingCartMapper;

    @Resource
    MallGoodsMapper mallGoodsMapper;


    @Override
    public String saveMallCartItem(MallShoppingCart mallShoppingCart) {
        MallShoppingCart temp = mallShoppingCartMapper.selectByUserIdAndGoodsId(mallShoppingCart.getUserId(), mallShoppingCart.getGoodsId());
        if (temp != null) {
            temp.setGoodsCount(temp.getGoodsCount() + mallShoppingCart.getGoodsCount());
            return updateMallCart(temp);
        }
        MallGoods mallGoods = mallGoodsMapper.getGoodById(mallShoppingCart.getGoodsId());
        //商品为空
        if (mallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = mallShoppingCartMapper.selectCountByUserId(mallShoppingCart.getUserId());
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //保存记录
        if (mallShoppingCartMapper.insertSelective(mallShoppingCart) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateMallCart(MallShoppingCart mallShoppingCart) {
        MallShoppingCart MallShoppingCartUpdate = mallShoppingCartMapper.selectByPrimaryKey(mallShoppingCart.getCartItemId());
        if (MallShoppingCartUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出单个商品的最大数量
        if (mallShoppingCart.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //todo 数量相同不会进行修改
        //todo userId不同不能修改
        MallShoppingCartUpdate.setGoodsCount(mallShoppingCart.getGoodsCount());
        MallShoppingCartUpdate.setUpdateTime(new Date());
        //修改记录
        if (mallShoppingCartMapper.updateByPrimaryKeySelective(MallShoppingCartUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public List<MallShoppingCartVO> getMyShoppingCartItems(Long userId) {
        List<MallShoppingCartVO> mallShoppingCartVOS = new ArrayList<>();
        //根据userId查询所有的购物项数据
        List<MallShoppingCart> mallShoppingCarts = mallShoppingCartMapper.selectByUserId(userId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty(mallShoppingCarts)) {
            //获取商品id
            List<Long> MallGoodsIds = mallShoppingCarts.stream().map(MallShoppingCart::getGoodsId).collect(Collectors.toList());
            //查询商品信息并做数据转换
            List<MallGoods> mallGoods = mallGoodsMapper.selectByPrimaryKeys(MallGoodsIds);
            Map<Long, MallGoods> mallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(mallGoods)) {
                mallGoodsMap = mallGoods.stream().collect(Collectors.toMap(MallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            //封装返回数据
            for (MallShoppingCart mallShoppingCart : mallShoppingCarts) {
                MallShoppingCartVO mallShoppingCartVO = new MallShoppingCartVO();
                MyBeanUtil.copyProperties(mallShoppingCart, mallShoppingCartVO);
                if (mallGoodsMap.containsKey(mallShoppingCart.getGoodsId())) {
                    MallGoods newBeeMallGoodsTemp = mallGoodsMap.get(mallShoppingCart.getGoodsId());
                    mallShoppingCartVO.setGoodsCoverImg(newBeeMallGoodsTemp.getGoodsCoverImg());
                    String goodsName = newBeeMallGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    mallShoppingCartVO.setGoodsName(goodsName);
                    mallShoppingCartVO.setSellingPrice(newBeeMallGoodsTemp.getSellingPrice());
                    mallShoppingCartVOS.add(mallShoppingCartVO);
                }
            }
        }
        return mallShoppingCartVOS==null?null:mallShoppingCartVOS;
    }

    @Override
    public String updateMallCartItem(MallShoppingCart mallShoppingCart) {
        MallShoppingCart newBeeMallShoppingCartItemUpdate = mallShoppingCartMapper.selectByPrimaryKey(mallShoppingCart.getCartItemId());
        if (newBeeMallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出最大数量
        if (newBeeMallShoppingCartItemUpdate.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        newBeeMallShoppingCartItemUpdate.setGoodsCount(mallShoppingCart.getGoodsCount());
        newBeeMallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (mallShoppingCartMapper.updateByPrimaryKeySelective(newBeeMallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Boolean deleteById(Long mallShoppingCartId) {
        return mallShoppingCartMapper.deleteByPrimaryKey(mallShoppingCartId) > 0;
    }

    @Override
    public Boolean deleteByIds(List<Long> mallShoppingCartIds) {
        return mallShoppingCartMapper.deleteBatch(mallShoppingCartIds) > 0;
    }
}
