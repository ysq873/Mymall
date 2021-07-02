package com.yyyysq.mall_gateway.controller.mall;


import com.alibaba.dubbo.config.annotation.Reference;
import com.yyyysq.mall_config.common.Constants;
import com.yyyysq.mall_config.common.MallException;
import com.yyyysq.mall_config.common.ServiceResultEnum;
import com.yyyysq.mall_config.utils.MyBeanUtil;
import com.yyyysq.mall_gateway.controller.vo.MallUserVO;
import com.yyyysq.mall_goods.VO.MallShoppingCartVO;
import com.yyyysq.mall_goods.serviceApi.MallShoppingCartService;
import com.yyyysq.mall_order.entity.MallOrder;
import com.yyyysq.mall_order.serviceApi.MallOrderService;
import com.yyyysq.mall_order.vo.MallOrderDetailVO;
import com.yyyysq.mall_user.dto.MallUserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class MallOrderController {

    @Reference
    private MallShoppingCartService mallShoppingCartService;
    @Reference
    private MallOrderService mallOrderService;

    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<MallShoppingCartVO> myShoppingCartItems = mallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (StringUtils.isEmpty(user.getAddress().trim())) {
            //无收货地址
            MallException.fail(ServiceResultEnum.NULL_ADDRESS_ERROR.getResult());
        }
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物车中无数据则跳转至错误页
            MallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        }
        MallUserDTO mallUserDTO = new MallUserDTO();
        MyBeanUtil.copyProperties(user,mallUserDTO);
        //保存订单并返回订单号
        String saveOrderResult = mallOrderService.saveOrder(mallUserDTO,myShoppingCartItems);
        //跳转到订单详情页
        return "redirect:/orders/" + saveOrderResult;
    }

    @GetMapping("/orders/{orderNo}")
    public String orderDetailPage(HttpServletRequest request, @PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        MallOrderDetailVO orderDetailVO = mallOrderService.getOrderDetailByOrderNo(orderNo, user.getUserId());
        if (orderDetailVO == null) {
            return "error/5xx";
        }
        request.setAttribute("orderDetailVO", orderDetailVO);
        return "mall/order-detail";
    }
    @GetMapping("/orders")
    public String orderListPage(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        params.put("userId", user.getUserId());
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //封装我的订单数据
        request.setAttribute("orderPageResult", mallOrderService.getMyOrders(params));
        request.setAttribute("path", "orders");
        return "mall/my-orders";
    }
    @GetMapping("/selectPayType")
    public String selectPayType(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        MallOrder mallOrder = mallOrderService.getMallOrderByOrderNo(orderNo);
        //todo 判断订单userId
        //todo 判断订单状态
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", mallOrder.getTotalPrice());
        return "mall/pay-select";
    }
    @GetMapping("/payPage")
    public String payOrder(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession, @RequestParam("payType") int payType) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        MallOrder mallOrder = mallOrderService.getMallOrderByOrderNo(orderNo);
        //todo 判断订单userId
        //todo 判断订单状态
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", mallOrder.getTotalPrice());
        if (payType == 1) {
            return "mall/alipay";
        } else {
            return "mall/wxpay";
        }
    }

}
