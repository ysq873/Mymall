package com.yyyysq.mall_order_service.service;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.yyyysq.mall_config.common.*;
import com.yyyysq.mall_config.utils.MyBeanUtil;
import com.yyyysq.mall_config.utils.NumberUtil;
import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_config.utils.PageResult;
import com.yyyysq.mall_goods.VO.MallShoppingCartVO;
import com.yyyysq.mall_goods.entity.MallGoods;
import com.yyyysq.mall_goods.entity.StockNumDTO;
import com.yyyysq.mall_goods.serviceApi.MallGoodsService;
import com.yyyysq.mall_goods.serviceApi.MallShoppingCartService;
import com.yyyysq.mall_order.entity.MallOrder;
import com.yyyysq.mall_order.entity.MallOrderItem;
import com.yyyysq.mall_order.serviceApi.MallOrderService;
import com.yyyysq.mall_order.vo.MallOrderDetailVO;
import com.yyyysq.mall_order.vo.MallOrderItemVO;
import com.yyyysq.mall_order.vo.MallOrderListVO;
import com.yyyysq.mall_order_service.dao.MallOrderItemMapper;
import com.yyyysq.mall_order_service.dao.MallOrderMapper;

import com.yyyysq.mall_user.dto.MallUserDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class MallOrderServiceImpl implements MallOrderService {

    @Reference
    MallShoppingCartService mallShoppingCartService;

    @Reference
    MallGoodsService mallGoodsService;

    @Resource
    MallOrderMapper mallOrderMapper;

    @Resource
    MallOrderItemMapper mallOrderItemMapper;

    @Override
    @Transactional
    //该方法涉及多表操作
   public String saveOrder(MallUserDTO user, List<MallShoppingCartVO> myShoppingCartItems){
        List<Long> itemIdList = myShoppingCartItems.stream().map(MallShoppingCartVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(MallShoppingCartVO::getGoodsId).collect(Collectors.toList());
        System.out.println(goodsIds);    //10742
        System.out.println(itemIdList);  //69
        //从商品表获取商品信息

        //检查是否包含已下架商品
        List<MallGoods> mallGoods = mallGoodsService.getMallGoodsByIds(goodsIds);
        List<MallGoods> goodsListNotSelling = mallGoods.stream()
                .filter(mallGoodsTemp -> mallGoodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling 对象非空则表示有下架商品
            MallException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        //根据商品id使用stream生成id和实体类映射
        Map<Long, MallGoods> mallGoodsMap = mallGoods.stream().collect(Collectors.toMap(MallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (MallShoppingCartVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!mallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                MallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > mallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                MallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        System.out.println(itemIdList);
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(mallGoods)) {
            if (mallShoppingCartService.deleteByIds(itemIdList)) {
                //获取商品库存对象(商品id，商品库存)
                List<StockNumDTO> stockNumDTOS = MyBeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = mallGoodsService.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    MallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                MallOrder mallOrder = new MallOrder();
                mallOrder.setOrderNo(orderNo);
                mallOrder.setUserId(user.getUserId());
                mallOrder.setUserAddress(user.getAddress());
                //总价
                for (MallShoppingCartVO mallShoppingCartVO : myShoppingCartItems) {
                    priceTotal += mallShoppingCartVO.getGoodsCount() * mallShoppingCartVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    MallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                mallOrder.setTotalPrice(priceTotal);
                String extraInfo = "";
                mallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (mallOrderMapper.insertSelective(mallOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<MallOrderItem> MallOrderItems = new ArrayList<>();
                    for (MallShoppingCartVO mallShoppingCartVO : myShoppingCartItems) {
                        MallOrderItem mallOrderItem = new MallOrderItem();
                        //使用BeanUtil工具类将mallShoppingCartItemVO中的属性复制到mallOrderItem对象中
                        MyBeanUtil.copyProperties(mallShoppingCartVO, mallOrderItem);
                        //mallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        mallOrderItem.setOrderId(mallOrder.getOrderId());
                        MallOrderItems.add(mallOrderItem);
                    }
                    //保存至数据库
                    if (mallOrderItemMapper.insertBatch(MallOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    MallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        MallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }

    @Override
    public PageResult getMallOrdersPage(PageQueryUtil pageUtil) {
        return null;
    }

    @Override
    public String updateOrderInfo(MallOrder mallOrder) {
        return null;
    }

    @Override
    public String checkDone(Long[] ids) {
        return null;
    }

    @Override
    public String checkOut(Long[] ids) {
        return null;
    }

    @Override
    public String closeOrder(Long[] ids) {
        return null;
    }

    @Override
    public MallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        MallOrder mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            List<MallOrderItem> orderItems = mallOrderItemMapper.selectByOrderId(mallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<MallOrderItemVO> mallOrderItemVOS = MyBeanUtil.copyList(orderItems, MallOrderItemVO.class);
                MallOrderDetailVO mallOrderDetailVO = new MallOrderDetailVO();
                MyBeanUtil.copyProperties(mallOrder, mallOrderDetailVO);
                mallOrderDetailVO.setOrderStatusString(MallOrderStatusEnum.getMallOrderStatusEnumByStatus(mallOrderDetailVO.getOrderStatus()).getName());
                mallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(mallOrderDetailVO.getPayType()).getName());
                mallOrderDetailVO.setMallOrderItemVOS(mallOrderItemVOS);
                return mallOrderDetailVO;
            }
        }
        return null;
    }

    @Override
    public MallOrder getMallOrderByOrderNo(String orderNo) {
        return mallOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult getMyOrders(Map<String, Object> pageUtil) {
        int total = mallOrderMapper.getTotalMallOrders(pageUtil);
        List<MallOrder> mallOrders = mallOrderMapper.findMallOrderList(pageUtil);
        List<MallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = MyBeanUtil.copyList(mallOrders, MallOrderListVO.class);
            //设置订单状态中文显示值
            for (MallOrderListVO mallOrderListVO : orderListVOS) {
                mallOrderListVO.setOrderStatusString(MallOrderStatusEnum.getMallOrderStatusEnumByStatus(mallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = mallOrders.stream().map(MallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<MallOrderItem> orderItems = mallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<MallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(MallOrderItem::getOrderId));
                for (MallOrderListVO mallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(mallOrderListVO.getOrderId())) {
                        List<MallOrderItem> orderItemListTemp = itemByOrderIdMap.get(mallOrderListVO.getOrderId());
                        //将NewBeeMallOrderItem对象列表转换成NewBeeMallOrderItemVO对象列表
                        List<MallOrderItemVO> mallOrderItemVOS = MyBeanUtil.copyList(orderItemListTemp, MallOrderItemVO.class);
                        mallOrderListVO.setNewBeeMallOrderItemVOS(mallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, Constants.ORDER_SEARCH_PAGE_LIMIT, 1);
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        return null;
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        return null;
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        return null;
    }

    @Override
    public List<MallOrderItemVO> getOrderItems(Long id) {
        return null;
    }
}
