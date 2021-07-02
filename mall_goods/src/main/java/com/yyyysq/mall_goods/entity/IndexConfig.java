package com.yyyysq.mall_goods.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class IndexConfig {

    private Long configId;

    private String configName;

    private Byte configType;

    private Long goodsId;

    private String redirectUrl;

    private Integer configRank;

    private Byte isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Integer createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer updateUser;
}
