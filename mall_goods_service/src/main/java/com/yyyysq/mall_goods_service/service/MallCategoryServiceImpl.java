package com.yyyysq.mall_goods_service.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yyyysq.mall_config.common.Constants;
import com.yyyysq.mall_config.common.MallCategoryLevelEnum;
import com.yyyysq.mall_config.common.ServiceResultEnum;
import com.yyyysq.mall_config.utils.MyBeanUtil;
import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_config.utils.PageResult;
import com.yyyysq.mall_goods.VO.MallIndexCategoryVO;
import com.yyyysq.mall_goods.VO.MallSearchGoodsCategoryVO;
import com.yyyysq.mall_goods.VO.SecondLevelCategoryVO;
import com.yyyysq.mall_goods.VO.ThirdLevelCategoryVO;
import com.yyyysq.mall_goods.dto.MallIndexCategoryDTO;
import com.yyyysq.mall_goods.entity.GoodsCategory;
import com.yyyysq.mall_goods.serviceApi.MallCategoryService;
import com.yyyysq.mall_goods_service.dao.MallCategoryMapper;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class MallCategoryServiceImpl implements MallCategoryService {

    @Resource
    MallCategoryMapper mallCategoryMapper;

    @Override
    public PageResult getCategorisPage(PageQueryUtil pageQueryUtil) {
        List<GoodsCategory> goodsCategoryList = mallCategoryMapper.findGoodsCategoryList(pageQueryUtil);
        int total = mallCategoryMapper.getTotalCategories(pageQueryUtil);
        //将分类列表，总页数，一页多少条，第几页封装到pageResult里面
        PageResult pageResult = new PageResult(goodsCategoryList, total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveCategory(GoodsCategory goodsCategory) {
        GoodsCategory temp = mallCategoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(),goodsCategory.getCategoryName());

        if(temp!=null){
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }

        if(mallCategoryMapper.insertSelective(goodsCategory)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }

        return ServiceResultEnum.ERROR.getResult();
    }

    @Override
    public String updateGoodsCategory(GoodsCategory goodsCategory) {
        GoodsCategory temp = mallCategoryMapper.selectByPrimaryKey(goodsCategory.getCategoryId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        GoodsCategory temp2 = mallCategoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(), goodsCategory.getCategoryName());
        if (temp2 != null && !temp2.getCategoryId().equals(goodsCategory.getCategoryId())) {
            //同名且不同id 不能继续修改
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        goodsCategory.setUpdateTime(new Date());
        if (mallCategoryMapper.updateByPrimaryKeySelective(goodsCategory) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if(ids.length<1){
            return false;
        }
        return mallCategoryMapper.deleteBatch(ids)>0;
    }

    @Override
    //当number为0时表示查找所有数据，只有当number>0时才会查找number数量的数据
    public List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel) {
        return mallCategoryMapper.selectByLevelAndParentIdsAndNumber(parentIds,categoryLevel,0);
    }

    @Override
    public GoodsCategory getGoodsCategoryById(Long categoryId) {
        return mallCategoryMapper.selectByPrimaryKey(categoryId);
    }

    @Override
    public List<MallIndexCategoryVO> getCategoriesForIndex() {
        List<MallIndexCategoryVO> categoryVOS = new ArrayList<>();
        //获取一级分类固定数量数据
        List<GoodsCategory> firstLevelCategories = mallCategoryMapper.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), MallCategoryLevelEnum.LEVEL_ONE.getLevel(), Constants.INDEX_CATEGORY_NUMBER);
        if(!CollectionUtils.isEmpty(firstLevelCategories)){
            //将列表转换为流数据再根据每个分类的id进行收集，用来找出第二级分类
            List<Long> firstLevelCategoryIds = firstLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
            //根据上面获取到的id来得到第二级分类集合
            List<GoodsCategory> secondLevelCategories = mallCategoryMapper.selectByLevelAndParentIdsAndNumber(firstLevelCategoryIds,MallCategoryLevelEnum.LEVEL_TWO.getLevel(),0);
            if(!CollectionUtils.isEmpty(secondLevelCategories)){
                //收集二级分类所有id，用来求第三级分类
                List<Long> secondLevelCategoryId = secondLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
                List<GoodsCategory> thirdLevelCategories = mallCategoryMapper.selectByLevelAndParentIdsAndNumber(secondLevelCategoryId,MallCategoryLevelEnum.LEVEL_THREE.getLevel(),0);
                //处理三级分类，将三级分类与二级分类绑定
                if(!CollectionUtils.isEmpty(thirdLevelCategories)) {
                    //将parentId和三级分类绑定
                    Map<Long, List<GoodsCategory>> thirdLevelCategoryMap = thirdLevelCategories.stream().collect(groupingBy(GoodsCategory::getParentId));
                    List<SecondLevelCategoryVO> secondLevelCategoryVOS = new ArrayList<>();
                    for (GoodsCategory secondCategory : secondLevelCategories) {
                        SecondLevelCategoryVO tempCategoryVO = new SecondLevelCategoryVO();
                        //这个组件可以将两个相似的对象的属性进行复制 (a,b) b中有的属性，a中一定要有，但是a可以有多余属性
                        MyBeanUtil.copyProperties(secondCategory, tempCategoryVO);
                        if (thirdLevelCategoryMap.containsKey(secondCategory.getCategoryId())) {
                            tempCategoryVO.setThirdLevelCategoryVOS(MyBeanUtil.copyList(thirdLevelCategoryMap.get(secondCategory.getCategoryId()), ThirdLevelCategoryVO.class));
                            secondLevelCategoryVOS.add(tempCategoryVO);
                        }
                    }
                    //将一级标签和二级标签绑定
                    if(!CollectionUtils.isEmpty((secondLevelCategoryVOS))){
                        //用已有的二级标签列表，并按照父级id进行分类
                        Map<Long,List<SecondLevelCategoryVO>> secondLevelCategoryMap = secondLevelCategoryVOS.stream().collect(groupingBy(SecondLevelCategoryVO::getParentId));
                        for(GoodsCategory goodsCategory : firstLevelCategories){
                            //一级标签
                            MallIndexCategoryVO mallIndexCategoryVO = new MallIndexCategoryVO();
                            MyBeanUtil.copyProperties(goodsCategory,mallIndexCategoryVO);
                            if(secondLevelCategoryMap.containsKey(mallIndexCategoryVO.getCategoryId())){
                               mallIndexCategoryVO.setSecondLevelCategoryVOS(secondLevelCategoryMap.get(goodsCategory.getCategoryId()));
                               categoryVOS.add(mallIndexCategoryVO);
                            }
                        }

                    }
                }
            }
            return categoryVOS;
        }else{
            return null;
        }
    }

    @Override
    public MallSearchGoodsCategoryVO getCategoriesForSearch(Long categoryId) {
        return null;
    }
}

