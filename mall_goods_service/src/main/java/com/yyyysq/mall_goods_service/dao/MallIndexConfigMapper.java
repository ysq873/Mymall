package com.yyyysq.mall_goods_service.dao;


import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_goods.entity.IndexConfig;

import java.util.List;

public interface MallIndexConfigMapper {

    public List<IndexConfig> findIndexConfigList(PageQueryUtil pageQueryUtil);

    public Integer getTotalIndexConfigs(PageQueryUtil pageQueryUtil);

    public Integer insertSelective(IndexConfig indexConfig);

    public List<IndexConfig> findIndexConfigsByTypeAndNum(int configType, int number);

    public IndexConfig selectByPrimaryKey(Long id);

    public Integer updateByPrimaryKeySelective(IndexConfig indexConfig);

    public Integer deleteBatch(Long[] ids);

}
