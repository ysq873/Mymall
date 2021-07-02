package com.yyyysq.mall_goods_service.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.yyyysq.mall_config.common.ServiceResultEnum;
import com.yyyysq.mall_config.utils.MyBeanUtil;
import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_config.utils.PageResult;
import com.yyyysq.mall_goods.VO.MallIndexConfigGoodsVO;
import com.yyyysq.mall_goods.entity.IndexConfig;
import com.yyyysq.mall_goods.entity.MallGoods;
import com.yyyysq.mall_goods.serviceApi.MallIndexConfigService;
import com.yyyysq.mall_goods_service.dao.MallGoodsMapper;
import com.yyyysq.mall_goods_service.dao.MallIndexConfigMapper;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MallIndexConfigServiceImpl implements MallIndexConfigService {

    @Resource
    MallIndexConfigMapper indexConfigMapper;

    @Resource
    MallGoodsMapper mallGoodsMapper;

    @Override
    public PageResult getConfigsPage(PageQueryUtil pageQueryUtil) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageQueryUtil);
        int total = indexConfigMapper.getTotalIndexConfigs(pageQueryUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        if (indexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return null;
    }

    @Override
    public List<MallIndexConfigGoodsVO> getConfigGoodsForIndex(int configType, int number) {
        List<MallIndexConfigGoodsVO> MallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<MallGoods> mallGoods =mallGoodsMapper.selectByPrimaryKeys(goodsIds);
            MallIndexConfigGoodsVOS = MyBeanUtil.copyList(mallGoods, MallIndexConfigGoodsVO.class);
            for (MallIndexConfigGoodsVO newBeeMallIndexConfigGoodsVO : MallIndexConfigGoodsVOS) {
                String goodsName = newBeeMallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = newBeeMallIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    newBeeMallIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    newBeeMallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return MallIndexConfigGoodsVOS;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
        return indexConfigMapper.deleteBatch(ids) > 0;
    }

}
