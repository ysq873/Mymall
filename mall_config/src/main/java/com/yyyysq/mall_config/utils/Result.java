package com.yyyysq.mall_config.utils;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Result<T> implements Serializable {

    private  static final long serialVersionUID=1L;

    //业务码
    private Integer resultCode;
    //信息
    private String message;
    //数据结果
    private T data;


    public Result(){

    }
    public Result(Integer resultCode,String message){
        this.resultCode = resultCode;
        this.message = message;
    }
}
