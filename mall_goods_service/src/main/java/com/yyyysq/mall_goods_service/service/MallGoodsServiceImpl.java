package com.yyyysq.mall_goods_service.service;


import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONObject;
import com.alibaba.dubbo.config.annotation.Service;
import com.yyyysq.mall_config.common.ServiceResultEnum;
import com.yyyysq.mall_config.utils.MyBeanUtil;
import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_config.utils.PageResult;
import com.yyyysq.mall_goods.VO.MallSearchGoodsVO;
import com.yyyysq.mall_goods.dto.MallSearchGoodsDTO;
import com.yyyysq.mall_goods.entity.MallGoods;
import com.yyyysq.mall_goods.entity.StockNumDTO;
import com.yyyysq.mall_goods.serviceApi.MallGoodsService;
import com.yyyysq.mall_goods_service.dao.MallGoodsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MallGoodsServiceImpl implements MallGoodsService {

    @Resource
    MallGoodsMapper goodsMapper;

    public String saveMallGoods(MallGoods goods) {
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public MallGoods getMallGoodsById(Long goodId) {
        return goodsMapper.getGoodById(goodId);
    }

    @Override
    public String updateMallGood(MallGoods goods) {
        //获取数据库中的该商品
        MallGoods temp = goodsMapper.getGoodById(goods.getGoodsId());
        //判断在数据库中是否存在这个数据
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public PageResult getMallGoodsPage(PageQueryUtil pageQueryUtil) {
        List<MallGoods> list = goodsMapper.findMallGoodsList(pageQueryUtil);
        int total = goodsMapper.getTotalMallGoods(pageQueryUtil);
        PageResult pageResult = new PageResult(list,total,pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public List<MallGoods> getMallGoodsByIds(List<Long> ids){
        List<MallGoods> goodsList = goodsMapper.selectByPrimaryKeys(ids);
        return goodsList==null?null:goodsList;
    }

    @Override
    public boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids,sellStatus) > 0;
    }

    @Override
    public PageResult<MallSearchGoodsDTO> searchMallGoods(Map<String,Object> params, int page, int limit){
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        pageQueryUtil.setPage(page);
        pageQueryUtil.setLimit(limit);
        List<MallGoods> goodsList = goodsMapper.findMallGoodsListBySearch(pageQueryUtil);
        int total = goodsMapper.getTotalMallGoodsBySearch(pageQueryUtil);
        log.info("dubbo-provider-PageQueryUtil:{}",pageQueryUtil);
        List<MallSearchGoodsDTO> mallSearchGoodsDTOS = MyBeanUtil.copyList(goodsList, MallSearchGoodsDTO.class);
        if (!CollectionUtils.isEmpty(mallSearchGoodsDTOS)) {
            for (MallSearchGoodsDTO mallSearchGoodsDTO : mallSearchGoodsDTOS) {
                String goodsName = mallSearchGoodsDTO.getGoodsName();
                String goodsIntro = mallSearchGoodsDTO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    mallSearchGoodsDTO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    mallSearchGoodsDTO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return new PageResult<>(mallSearchGoodsDTOS, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
    }

    @Override
    public Integer updateStockNum(List<StockNumDTO> stockNumDTOS) {
        return goodsMapper.updateStockNum(stockNumDTOS);
    }
}
