package com.yyyysq.mall_gateway.controller.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class MallOrderDetailVO implements Serializable {
    private String orderNo;

    private Integer totalPrice;

    private Byte payStatus;

    private String payStatusString;

    private Byte payType;

    private String payTypeString;

    private Date payTime;

    private Byte orderStatus;

    private String orderStatusString;

    private String userAddress;

    private Date createTime;

    private List<MallOrderItemVO> MallOrderItemVOS;
}
