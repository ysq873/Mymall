package com.yyyysq.mall_order.serviceApi;

import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_config.utils.PageResult;
import com.yyyysq.mall_goods.VO.MallShoppingCartVO;
import com.yyyysq.mall_goods.VO.MallUserVO;
import com.yyyysq.mall_order.dto.MallOrderDetailDTO;
import com.yyyysq.mall_order.entity.MallOrder;
import com.yyyysq.mall_order.vo.MallOrderDetailVO;
import com.yyyysq.mall_order.vo.MallOrderItemVO;
import com.yyyysq.mall_user.dto.MallUserDTO;

import java.util.List;
import java.util.Map;

public interface MallOrderService {

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getMallOrdersPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     *
     * @param mallOrder
     * @return
     */
    String updateOrderInfo(MallOrder mallOrder);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    /**
     * 保存订单
     *
     * @param user
     * @param myShoppingCartItems
     * @return
     */
    String saveOrder(MallUserDTO user, List<MallShoppingCartVO> myShoppingCartItems);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    MallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    MallOrder getMallOrderByOrderNo(String orderNo);

    /**
     * 我的订单列表
     *
     * @param params
     * @return
     */
    PageResult getMyOrders(Map<String,Object> params);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<MallOrderItemVO> getOrderItems(Long id);


}
