package com.yyyysq.mall_config.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 分页查询参数
 *
 */
@Data
public class PageQueryUtil extends HashMap<String, Object> implements Serializable {
    //当前页码
    private int page;
    //每页条数
    private int limit;

    public PageQueryUtil(Map<String, Object> params) {
        this.putAll(params);

        //分页参数
        this.page = Integer.parseInt(params.get("page").toString()); //请求页数
        this.limit = Integer.parseInt(params.get("limit").toString()); //一页多少行
        this.put("start", (page - 1) * limit); //数据库中因为从0开始所以页数-1
        this.put("page", page); //前端显示的当前页数
        this.put("limit", limit);
    }

}
