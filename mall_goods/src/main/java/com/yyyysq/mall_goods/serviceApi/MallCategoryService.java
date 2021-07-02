package com.yyyysq.mall_goods.serviceApi;



import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_config.utils.PageResult;
import com.yyyysq.mall_goods.VO.MallIndexCategoryVO;
import com.yyyysq.mall_goods.VO.MallSearchGoodsCategoryVO;
import com.yyyysq.mall_goods.dto.MallIndexCategoryDTO;
import com.yyyysq.mall_goods.dto.MallSearchGoodsCategoryDTO;
import com.yyyysq.mall_goods.entity.GoodsCategory;

import java.util.List;

public interface MallCategoryService {

    public PageResult getCategorisPage(PageQueryUtil pageQueryUtil);

    public String saveCategory(GoodsCategory goodsCategory);

    public String updateGoodsCategory(GoodsCategory goodsCategory);

    public Boolean deleteBatch(Integer[] ids);

    public List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);

    public GoodsCategory getGoodsCategoryById(Long categoryId);

    public List<MallIndexCategoryVO> getCategoriesForIndex();

    public MallSearchGoodsCategoryVO getCategoriesForSearch(Long categoryId);

}
