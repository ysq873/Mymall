package com.yyyysq.mall_goods.serviceApi;


import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_config.utils.PageResult;
import com.yyyysq.mall_goods.entity.MallGoods;
import com.yyyysq.mall_goods.entity.StockNumDTO;

import java.util.List;
import java.util.Map;

public interface MallGoodsService {

    /**
     * 保存商品
     * @param goods
     * @return
     */
    public String saveMallGoods(MallGoods goods);

    /**
     * 通过id获取商品
     * @param goodsId
     * @return
     */
    public MallGoods getMallGoodsById(Long goodsId);


    /**
     * 更新商品信息
     * @param goods
     * @return
     */
    public String updateMallGood(MallGoods goods);

    /**
     * 获取商品页数
     * @param pageQueryUtil
     * @return
     */
    public PageResult getMallGoodsPage(PageQueryUtil pageQueryUtil);

    /**
     * 批量处理上架状态
     * @param ids
     * @param sellStatus
     * @return
     */
    public boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

    /**
     * 获取搜索商品信息
     * @param map,page,limit
     * @return
     */
    public PageResult searchMallGoods(Map<String,Object> map, int page, int limit);

    /**
     * 根据id列表获取商品信息
     * @param ids
     * @return
     */
    public List<MallGoods> getMallGoodsByIds(List<Long> ids);

    /**
     * 更改存货
     * @param stockNumDTOS
     * @return
     */
    public Integer updateStockNum(List<StockNumDTO> stockNumDTOS);

}
