package com.yyyysq.mall_order_service.dao;


import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_order.entity.MallOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MallOrderMapper {

    int deleteByPrimaryKey(Long orderId);

    int insert(MallOrder record);

    int insertSelective(MallOrder record);

    MallOrder selectByPrimaryKey(Long orderId);

    MallOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(MallOrder record);

    int updateByPrimaryKey(MallOrder record);

    List<MallOrder> findMallOrderList(Map<String, Object> pageUtil);

    int getTotalMallOrders(Map<String, Object> pageUtil);

    List<MallOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);


}