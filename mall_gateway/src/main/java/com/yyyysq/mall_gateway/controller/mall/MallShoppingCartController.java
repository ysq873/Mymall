package com.yyyysq.mall_gateway.controller.mall;


import com.alibaba.dubbo.config.annotation.Reference;
import com.yyyysq.mall_config.common.Constants;
import com.yyyysq.mall_config.common.ServiceResultEnum;
import com.yyyysq.mall_config.utils.Result;
import com.yyyysq.mall_config.utils.ResultGenerator;
import com.yyyysq.mall_gateway.controller.vo.MallUserVO;
import com.yyyysq.mall_goods.VO.MallShoppingCartVO;
import com.yyyysq.mall_goods.entity.MallShoppingCart;
import com.yyyysq.mall_goods.serviceApi.MallShoppingCartService;
import com.yyyysq.mall_user.dto.MallUserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MallShoppingCartController {

    @Reference
    MallShoppingCartService mallShoppingCartService;

    @PostMapping("/shop-cart")
    @ResponseBody
    public Result saveMallShoppingCartItem(@RequestBody MallShoppingCart MallShoppingCartItem,
                                           HttpSession httpSession) {
        MallUserVO user= (MallUserVO)httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        System.out.println("控制层"+user);
        MallShoppingCartItem.setUserId(user.getUserId());
        String saveResult = mallShoppingCartService.saveMallCartItem(MallShoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request,
                               HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        int itemsTotal = 0;
        int priceTotal = 0;
        List<MallShoppingCartVO> myShoppingCartItems = mallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物项总数
            itemsTotal = myShoppingCartItems.stream().mapToInt(MallShoppingCartVO::getGoodsCount).sum();
            if (itemsTotal < 1) {
                return "error/5xx";
            }
            //总价
            for (MallShoppingCartVO mallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += mallShoppingCartItemVO.getGoodsCount() * mallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/5xx";
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/shoppingCart";
    }

    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updateMallShoppingCartItem(@RequestBody MallShoppingCart mallShoppingCart,
                                                   HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        mallShoppingCart.setUserId(user.getUserId());
        String updateResult = mallShoppingCartService.updateMallCartItem(mallShoppingCart);
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{mallShoppingCartItemId}")
    @ResponseBody
    public Result updateMallShoppingCartItem(@PathVariable("mallShoppingCartItemId") Long mallShoppingCartId,
                                                   HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Boolean deleteResult = mallShoppingCartService.deleteById(mallShoppingCartId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    public String settlePage(HttpServletRequest request,
                             HttpSession httpSession) {
        int priceTotal = 0;
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<MallShoppingCartVO> myShoppingCartItems = mallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            return "mall/shoppingCart";
        } else {
            //总价
            for (MallShoppingCartVO newBeeMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += newBeeMallShoppingCartItemVO.getGoodsCount() * newBeeMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/5xx";
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/order";
    }
}
