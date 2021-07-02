package com.yyyysq.mall_goods_service.dao;


import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_goods.entity.GoodsCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface MallCategoryMapper {

    //获取所有的分类列表
    public List<GoodsCategory> findGoodsCategoryList(PageQueryUtil pageQueryUtil);

    //获取总分类数
    public Integer getTotalCategories(PageQueryUtil pageQueryUtil);

    //通过分类的等级和分类名获取分类
    public GoodsCategory selectByLevelAndName(@RequestParam("categoryLevel") Byte categoryLevel, @RequestParam("categoryName") String categoryName);

    //插入新的分类
    public Integer insertSelective(GoodsCategory goodsCategory);

    //根据id获取分类
    public GoodsCategory selectByPrimaryKey(Long categoryId);

    //根据分类id修改分类
    public Integer updateByPrimaryKeySelective(GoodsCategory goodsCategory);

    //根据id数组删除选择的用户
    public Integer deleteBatch(Integer[] ids);

    //因为有多个参数，需要使用注释@Param给mapper传递参数名
    public List<GoodsCategory> selectByLevelAndParentIdsAndNumber(@Param("parentIds") List<Long> parentIds, @Param("categoryLevel") Integer categoryLevel, @Param("number") Integer number);
}
